package com.luhanlin.netty.rpc.consumer;

import com.luhanlin.netty.rpc.api.IRPCService;
import com.luhanlin.netty.rpc.common.config.KeeperConfig;
import com.luhanlin.netty.rpc.register.handler.RpcRegistryHandler;
import com.luhanlin.netty.rpc.register.handler.impl.ZookeeperRegistryHandler;

import java.util.ArrayList;

/**
 * 消费端启动类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class ConsumerBootstrap {

    private static final String ZOOKEEPER_ADDRESS = "192.168.134.121:2181";
    private static final int DEFAULT_REPORT_INTERVAL = 5;


    public static void main(String[] args) throws InterruptedException {
        ArrayList<Class<?>> serviceList = new ArrayList<>();
        serviceList.add(IRPCService.class);
        KeeperConfig config = KeeperConfig.getInstance();
        config.setConsumerSide(true);
        config.setInterval(DEFAULT_REPORT_INTERVAL);

        RpcRegistryHandler rpcRegistryHandler = new ZookeeperRegistryHandler(ZOOKEEPER_ADDRESS);
        RPCConsumer consumer = new RPCConsumer(rpcRegistryHandler, serviceList);

        IRPCService rpcService = (IRPCService) consumer.createProxy(IRPCService.class);

        while (true) {
            Thread.sleep(3000);
            rpcService.hello("are you ok?");
        }
    }
}
