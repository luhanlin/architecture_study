package com.luhanlin.netty.rpc.provider;

import com.luhanlin.netty.rpc.common.config.KeeperConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 服务注册启动类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@ComponentScan(value = "com.luhanlin.netty")
@SpringBootApplication
public class ProviderBootStrap {

    private static final String ZOOKEEPER_ADDRESS = "192.168.134.121:2181";

    public static void main(String[] args) {

        int port = 8082;
        KeeperConfig keeperConfig = KeeperConfig.getInstance();
        keeperConfig.setPort(port);
        keeperConfig.setZkAddr(ZOOKEEPER_ADDRESS);
        keeperConfig.setProviderSide(true);

        SpringApplication.run(ProviderBootStrap.class, args);
    }
}
