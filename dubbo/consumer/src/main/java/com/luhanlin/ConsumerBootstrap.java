package com.luhanlin;

import com.luhanlin.service.ConsumerService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 消费启动类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class ConsumerBootstrap {

    public static void main(String[] args) throws IOException, InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        ConsumerService service = context.getBean(ConsumerService.class);

        final ExecutorService executeThreadPool = Executors.newFixedThreadPool(10);
        String world = service.hello("world", 100);
        System.out.println(world);
        while (true) {
            executeThreadPool.submit(() -> service.hello("world", 100));
            executeThreadPool.submit(() -> service.hello2("world", 100));
            executeThreadPool.submit(() -> service.hello3("world", 100));
            TimeUnit.MILLISECONDS.sleep(20);
        }
    }

    @Configuration
    @PropertySource("classpath:/dubbo-consumer.properties")
    @ComponentScan("com.luhanlin.service")
    @EnableDubbo
    static class ConsumerConfiguration {

    }
}
