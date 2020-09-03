package com.luhanlin.netty.rpc.provider;

import com.luhanlin.netty.rpc.api.IRPCService;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-03 14:31]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RPCServiceImpl implements IRPCService {

    public String hello(String content) {
        return "Hello " + content + "!";
    }
}
