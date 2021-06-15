package com.luhanlin.service.impl;

import com.luhanlin.servive.IHelloService;
import org.apache.dubbo.config.annotation.Service;

import java.util.Random;

/**
 * 接口实现类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Service
public class HelloServiceImpl implements IHelloService {

    @Override
    public String sayHello(String name, int timeToWait) {
        return result(name, timeToWait, "hello:");
    }

    @Override
    public String sayHello2(String name, int timeToWait) {
        return result(name, timeToWait, "hello2:");
    }

    @Override
    public String sayHello3(String name, int timeToWait) {
        return result(name, timeToWait, "hello3:");
    }

    private String result(String name, int timeToWait, String prefix) {
        int random = new Random().nextInt(timeToWait);
        try {
            Thread.sleep(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return prefix + name;
    }

}
