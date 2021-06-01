package com.luhanlin.netty.rpc.consumer.loadbalance;

import com.luhanlin.netty.rpc.consumer.client.RpcClient;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡抽象类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public abstract class AbstractLoadBalance implements LoadBalanceStrategy {

    @Override
    public RpcClient route(Map<String, List<RpcClient>> clientPool, String serviceClassName) {
        if (clientPool == null || clientPool.isEmpty()) {
            return null;
        }

        List<RpcClient> rpcClients = clientPool.get(serviceClassName);
        if (rpcClients == null) {
            return null;
        }

        // 选择客户端
        return doGetRpcClient(rpcClients);
    }

    public abstract RpcClient doGetRpcClient(List<RpcClient> rpcClients);
}
