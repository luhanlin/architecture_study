package com.luhanlin.netty.rpc.consumer;

import com.luhanlin.netty.rpc.api.IRPCService;
import com.luhanlin.netty.rpc.consumer.proxy.RPCProxy;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-03 15:44]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RPCConsumer {

    public static void main(String[] args) {
        IRPCService irpcService = RPCProxy.create(IRPCService.class);

        System.out.println(irpcService.hello("xiao ming"));
    }
}
