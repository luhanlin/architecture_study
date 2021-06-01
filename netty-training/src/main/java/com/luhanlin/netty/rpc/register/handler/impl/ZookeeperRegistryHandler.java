package com.luhanlin.netty.rpc.register.handler.impl;

import com.luhanlin.netty.rpc.common.config.KeeperConfig;
import com.luhanlin.netty.rpc.monitor.metrics.RequestMetrics;
import com.luhanlin.netty.rpc.common.listener.NodeChangeListener;
import com.luhanlin.netty.rpc.common.utils.LocalZookeeperConnection;
import com.luhanlin.netty.rpc.register.handler.RpcRegistryHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 基于zookeeper实现的注册中心<br />
 * zookeeper保存路径：/lu_rpc/com.luhanlin.netty.rpc.api.IRPCService/provider/127.0.0.1:8081
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class ZookeeperRegistryHandler implements RpcRegistryHandler {

    private static final String LU_RPC_ZK_ROOT = "/lu_rpc/";
    private static final String ZK_PATH_SPLIT = "/";
    private static final String CHARSET = "UTF-8";
    private static final List<NodeChangeListener> listenerList = new ArrayList<>();
    private static final ScheduledExecutorService REPORT_WORKER = Executors.newScheduledThreadPool(1);

    private List<String> serviceList = new CopyOnWriteArrayList<>();

    private CuratorFramework client;

    public ZookeeperRegistryHandler(String url) {
        // 1. 连接注册中心
        client = LocalZookeeperConnection.getCuratorFramework(url);

        // 2. 定时统计请求耗时
        KeeperConfig keeperConfig = KeeperConfig.getInstance();
        int interval = keeperConfig.getInterval();
        boolean consumerSide = keeperConfig.isConsumerSide();

        if (consumerSide && interval > 0) {
            REPORT_WORKER.scheduleAtFixedRate(()->{
                ConcurrentHashMap<String, RequestMetrics.Metrics> metricMap = RequestMetrics.getInstance().getMetricMap();

                if (metricMap.isEmpty()) {
                    return;
                }

                System.out.println("自动上报节点耗时日志:" + metricMap);
                metricMap.forEach((key, metrics) -> {
                    String metricsPath = metricsPath(metrics.getServiceName());
                    if (!exists(metricsPath)) {
                        create(metricsPath, false);
                    }

                    String costPath = metricsPath + ZK_PATH_SPLIT + key;

                    // 计算节点世界是否已经超过了上报间隔时间
                    if ((metrics.getStart() + interval * 1000) < System.currentTimeMillis()) {
                        // 超时，需要移除节点
                        if (exists(costPath)) {
                            remove(costPath);
                        }
                    } else {
                        updateWithData(costPath, metrics.getCost().toString(), false);
                    }
                });
            }, interval, interval, TimeUnit.SECONDS);
        }
    }

    @Override
    public boolean register(String service, String ip, int port) {
        String providerPath = providerPath(service);
        // ZK服务根路劲
        if (!exists(providerPath)) {
            create(providerPath, false);
        }

        // 服务注册
        String serverPath = providerPath + ZK_PATH_SPLIT + ip + ":" + port;
        create(serverPath, true);
        System.out.println(service + "注册成功，地址：" + serverPath);
        return true;
    }

    @Override
    public List<String> discovery(String serviceName) {
        String providerPath = providerPath(serviceName);
        try {
            if (serviceList.isEmpty()) {
                System.out.println("首次从注册中心查找服务地址...");
                serviceList = client.getChildren().forPath(providerPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 监听服务端的节点变化
        System.out.println("节点变更监听：" + serviceName + "[" + providerPath + "]");
        this.registryWatch(serviceName, providerPath);
        return serviceList;
    }

    private void registryWatch(String serviceName, String providerPath) {
        TreeCache nodeCache = new TreeCache(client, providerPath);

        nodeCache.getListenable().addListener((client, pathChildrenCacheEvent) -> {
            serviceList = client.getChildren().forPath(providerPath);
            listenerList.forEach(nodeChangeListener -> {
                System.out.println("=================节点变化，开始通知业务, event:" + pathChildrenCacheEvent);
                nodeChangeListener.notify(serviceName, serviceList, pathChildrenCacheEvent);
            });
        });
        try {
            nodeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addListener(NodeChangeListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void destroy() {
        if (null != client) {
            client.close();
        }
    }

    private String providerPath(String service) {
        return LU_RPC_ZK_ROOT + service + ZK_PATH_SPLIT + "provider";
    }

    private String metricsPath(String service) {
        return LU_RPC_ZK_ROOT + service + ZK_PATH_SPLIT + "metrics";
    }
    public boolean exists(String path) {
        try {
            if (client.checkExists().forPath(path) != null) {
                return true;
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return false;
    }

    private void create(String path, Boolean ephemeral) {
        create(path, null, ephemeral);
    }

    private void create(String path, String data, Boolean ephemeral) {
        if (ephemeral) {
            this.createEphemeral(path, data);
        } else {
            this.createPersistent(path, data);
        }
    }

    private void remove(String costPath) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(costPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateWithData(String costPath, String cost, boolean ephemeral) {
        if (exists(costPath)) {
            update(costPath, cost);
        } else {
            create(costPath, cost, ephemeral);
        }
    }

    private void update(String costPath, String cost) {
        try {
            byte[] data = cost.getBytes(CHARSET);
            client.setData().forPath(costPath, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createEphemeral(String path) {
        createEphemeral(path, null);
    }

    private void createEphemeral(String path, String data) {
        try {
            if (data != null) {
                byte[] dataBytes = data.getBytes(CHARSET);
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, dataBytes);
            } else {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }


    }

    private void createPersistent(String path) {
        createPersistent(path, null);
    }

    private void createPersistent(String path, String data) {
        try {
            if (data != null) {
                byte[] dataBytes = data.getBytes(CHARSET);
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, dataBytes);
            } else {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
            }
        } catch (KeeperException.NodeExistsException e) {
            System.out.println("路径[" + path + "]已存在");
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
