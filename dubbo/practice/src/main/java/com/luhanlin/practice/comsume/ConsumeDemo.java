package com.luhanlin.practice.comsume;

import com.luhanlin.practice.api.IHelloService;
import org.apache.dubbo.config.annotation.DubboReference;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-18 10:59]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ConsumeDemo {

    @DubboReference
    private IHelloService helloService;

    public static void main(String[] args) {

    }
}
