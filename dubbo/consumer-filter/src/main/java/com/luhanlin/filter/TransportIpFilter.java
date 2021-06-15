package com.luhanlin.filter;

import com.luhanlin.util.TransportHolder;
import com.luhanlin.util.WebRequestHolder;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * 设置 request 中我们需要的信息到 dubbo的全局环境中，方便后面服务的使用
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Activate(group = {CommonConstants.CONSUMER })
public class TransportIpFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        TransportHolder.setRequestIp(WebRequestHolder.getRequest());
        return invoker.invoke(invocation);
    }
}
