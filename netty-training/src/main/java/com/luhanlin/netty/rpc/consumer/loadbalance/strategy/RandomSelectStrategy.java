package com.luhanlin.netty.rpc.consumer.loadbalance.strategy;

import com.luhanlin.netty.rpc.consumer.client.RpcClient;
import com.luhanlin.netty.rpc.consumer.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

/**
 * 随机选择
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class RandomSelectStrategy extends AbstractLoadBalance {

    @Override
    public RpcClient doGetRpcClient(List<RpcClient> rpcClients) {
        Random random = new Random();
        return rpcClients.get(random.nextInt(rpcClients.size()));
    }
}
