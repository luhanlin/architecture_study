package com.luhanlin.netty.rpc.register.handler.impl;

import com.luhanlin.netty.rpc.common.config.KeeperConfig;
import com.luhanlin.netty.rpc.common.idle.RequestMetrics;
import com.luhanlin.netty.rpc.common.listener.NodeChangeListener;
import com.luhanlin.netty.rpc.common.utils.LocalZookeeperConnection;
import com.luhanlin.netty.rpc.register.handler.RpcRegistryHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
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
    private static final List<NodeChangeListener> listenerList = new ArrayList<>();
    private static final ScheduledExecutorService REPORT_WORKER = Executors.newScheduledThreadPool(1);

    private List<String> serviceList = new CopyOnWriteArrayList<>();

    private CuratorFramework client;

    public ZookeeperRegistryHandler(String url) {
        // 1. 连接注册中心
        client = LocalZookeeperConnection.getCuratorFramework(url);

        // 2. 定时上报心跳
//        KeeperConfig keeperConfig = KeeperConfig.getInstance();
//        int interval = keeperConfig.getInterval();
//        boolean consumerSide = keeperConfig.isConsumerSide();
//
//        if (consumerSide && interval > 0) {
//            REPORT_WORKER.scheduleAtFixedRate(()->{
//                ConcurrentHashMap<String, RequestMetrics.Metrics> metricMap = RequestMetrics.getInstance().getMetricMap();
//
//
//
//            }, interval, interval, TimeUnit.SECONDS);
//        }


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
        this.registryWatch(serviceName, providerPath);
        return serviceList;
    }

    private void registryWatch(String serviceName, String providerPath) {
        PathChildrenCache nodeCache = new PathChildrenCache(client, providerPath, true);

        nodeCache.getListenable().addListener((client, pathChildrenCacheEvent) -> {
            serviceList = client.getChildren().forPath(providerPath);
            listenerList.forEach(nodeChangeListener -> {
                System.out.println("节点变化，开始通知业务");
                nodeChangeListener.notify(serviceName, serviceList, pathChildrenCacheEvent);
            });
        });
        try {
            nodeCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
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
        if (ephemeral) {
            this.createEphemeral(path);
        } else {
            this.createPersistent(path);
        }
    }

    private void createEphemeral(String path) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void createPersistent(String path) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
        } catch (KeeperException.NodeExistsException e) {
            System.out.println("路径[" + path + "]已存在");
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
