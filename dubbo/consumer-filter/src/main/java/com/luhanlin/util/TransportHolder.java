package com.luhanlin.util;

import org.apache.dubbo.rpc.RpcContext;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletRequest;
import java.util.Map;

/**
 * DUBBO 全局环境数据存取
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class TransportHolder {

    private static final String REQUEST_IP = "request_ip";

    public static void setRequestIp (ServletRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            return;
        }
        final Map<String, String> attachments = RpcContext.getContext().getAttachments();
        attachments.put(REQUEST_IP, request.getRemoteAddr());
    }

    public static String getRequestIp() {
        final Map<String, String> attachments = RpcContext.getContext().getAttachments();
        return attachments.get(REQUEST_IP);
    }

}
