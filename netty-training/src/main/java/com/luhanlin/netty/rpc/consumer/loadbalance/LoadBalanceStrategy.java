package com.luhanlin.netty.rpc.consumer.loadbalance;

import com.luhanlin.netty.rpc.consumer.client.RpcClient;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡策略
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public interface LoadBalanceStrategy {

    /**
     * 路由到请求客户端
     * @param clientPool
     * @param serviceClassName
     * @return
     */
    RpcClient route(Map<String, List<RpcClient>> clientPool, String serviceClassName);
}
