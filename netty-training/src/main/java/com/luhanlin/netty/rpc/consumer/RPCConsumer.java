package com.luhanlin.netty.rpc.consumer;

import com.luhanlin.netty.rpc.common.idle.RequestMetrics;
import com.luhanlin.netty.rpc.common.listener.NodeChangeListener;
import com.luhanlin.netty.rpc.consumer.client.RpcClient;
import com.luhanlin.netty.rpc.consumer.proxy.RPCProxy;
import com.luhanlin.netty.rpc.register.handler.RpcRegistryHandler;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <类详细描述> Rpc 消费类
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-03 15:44]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RPCConsumer implements NodeChangeListener {

    // 服务对应的客户端
    public static final Map<String, List<RpcClient>> CLIENT_POOL = new ConcurrentHashMap<>();

    // 注册中心进行服务发现
    private RpcRegistryHandler rpcRegistryHandler;

    public RPCConsumer(RpcRegistryHandler rpcRegistryHandler, List<Class<?>> interClassList) {
        this.rpcRegistryHandler = rpcRegistryHandler;

        // 进行服务客户端的初始化
        for (Class<?> interClass : interClassList) {
            String interClassName = interClass.getName();
            List<String> discovery = rpcRegistryHandler.discovery(interClassName);

            List<RpcClient> rpcClients = new ArrayList<>();

            for (String server : discovery) {
                String[] split = server.split(":");
                RpcClient rpcClient = new RpcClient(split[0], Integer.parseInt(split[1]), interClassName);
                try {
                    rpcClient.initClient();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rpcClients.add(rpcClient);
            }
            CLIENT_POOL.put(interClassName, rpcClients);
        }
    }

    public Object createProxy(final Class<?> serviceClass) {
        return RPCProxy.create(serviceClass);
    }

    @Override
    public void notify(String service, List<String> serviceList, PathChildrenCacheEvent pathChildrenCacheEvent) {
        List<RpcClient> rpcClients = CLIENT_POOL.get(service);

        PathChildrenCacheEvent.Type eventType = pathChildrenCacheEvent.getType();
        System.out.println("收到节点变更通知:" + eventType + "----" + rpcClients + "---" + service + "---" + serviceList);
        String path = pathChildrenCacheEvent.getData().getPath();
        String instanceConfig = path.substring(path.lastIndexOf("/") + 1);

        // 增加节点
        if (PathChildrenCacheEvent.Type.CHILD_ADDED.equals(eventType)
                || PathChildrenCacheEvent.Type.CONNECTION_RECONNECTED.equals(eventType)) {
            if (CollectionUtils.isEmpty(rpcClients)) {
                rpcClients = new ArrayList<>();
            }
            String[] address = instanceConfig.split(":");
            RpcClient client = new RpcClient(address[0], Integer.parseInt(address[1]));
            try {
                client.initClient();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rpcClients.add(client);

            // 节点耗时统计
            RequestMetrics.getInstance().addNode(address[0], Integer.parseInt(address[1]), service);
            System.out.println("新增节点:" + instanceConfig);
        } else if (PathChildrenCacheEvent.Type.CHILD_REMOVED.equals(eventType)
                || PathChildrenCacheEvent.Type.CONNECTION_SUSPENDED.equals(eventType)
                || PathChildrenCacheEvent.Type.CONNECTION_LOST.equals(eventType)) {
            // 移除节点
            if (!CollectionUtils.isEmpty(rpcClients)) {
                String[] address = instanceConfig.split(":");
                for (int i = 0; i < rpcClients.size(); i++) {
                    RpcClient item = rpcClients.get(i);
                    if (item.getIp().equalsIgnoreCase(address[0]) && Integer.parseInt(address[1]) == item.getPort()) {

                        // 关闭连接
                        item.close();

                        // 从可用列表中移除
                        rpcClients.remove(item);
                        System.out.println("移除节点:" + instanceConfig);
                        RequestMetrics.getInstance().removeNode(address[0], Integer.parseInt(address[1]));
                    }
                }
            }
        }
    }
}
