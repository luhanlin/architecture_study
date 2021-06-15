package com.luhanlin;

import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

/**
 * 服务端启动类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class ProviderBootstrap {

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProviderConfiguration.class);
        context.start();
        System.in.read();
    }

    @Configuration
    @EnableDubbo(scanBasePackages = "com.luhanlin.service.impl")
    @PropertySource("classpath:/dubbo-provider.properties")
    static  class  ProviderConfiguration{
        @Bean
        public RegistryConfig registryConfig(){
            RegistryConfig  registryConfig  = new RegistryConfig();
            registryConfig.setAddress("zookeeper://127.0.0.1:2181?timeout=10000");
            return   registryConfig;
        }
    }
}
