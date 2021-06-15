package com.luhanlin.servive;

/**
 * 服务接口提供
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public interface IHelloService {

    String sayHello(String name, int timeToWait);

    String sayHello2(String name, int timeToWait);

    String sayHello3(String name, int timeToWait);
}
