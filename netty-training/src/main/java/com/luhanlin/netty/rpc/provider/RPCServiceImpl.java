package com.luhanlin.netty.rpc.provider;

import com.luhanlin.netty.rpc.api.IRPCService;
import com.luhanlin.netty.rpc.common.entity.Test;
import lombok.Data;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-03 14:31]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RPCServiceImpl implements IRPCService {

    public Object hello(String content) {
        Test test = new Test();
        test.setMemo("接受请求啦");
        test.setContent(content);
        return test;
    }
}
