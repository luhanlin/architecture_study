package com.luhanlin.practice.provider;

import com.luhanlin.practice.api.IHelloService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-17 11:20]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@DubboService
public class HelloServiceProvider implements IHelloService {

    @Override
    public void hello() {
        System.out.println("hello :");
    }

}
