package com.luhanlin.service;

import com.luhanlin.servive.IHelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * 服务接口
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Service
public class ConsumerService {

    @Reference
    private IHelloService helloService;

    public String hello(String name, int timeToWait) {
        return helloService.sayHello(name, timeToWait);
    }

    public String hello2(String name, int timeToWait) {
        return helloService.sayHello2(name, timeToWait);
    }

    public String hello3(String name, int timeToWait) {
        return helloService.sayHello3(name, timeToWait);
    }
}
