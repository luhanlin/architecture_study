package com.luhanlin.service.impl;

import com.luhanlin.servive.IRequestIpService;
import com.luhanlin.util.TransportHolder;
import org.apache.dubbo.config.annotation.Service;

/**
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Service
public class RequestIpServiceImpl implements IRequestIpService {

    @Override
    public String outRequestIp(String name) {
        String requestIp = TransportHolder.getRequestIp();
        System.out.println("请求服务的ip地址为：" + requestIp);
        return "hello:" + name;
    }
}
