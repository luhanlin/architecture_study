package com.luhanlin.netty.rpc.common.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-08 16:53]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LocalZookeeperConnection {

    private static final String ZOOKEEPER_ADDRESS = "192.168.134.121:2181";

    private static final CuratorFramework curatorFramework;

    static {
        curatorFramework = getClient(ZOOKEEPER_ADDRESS);
    }

    public static CuratorFramework getCuratorFramework() {
        return getCuratorFramework(ZOOKEEPER_ADDRESS);
    }

    public static CuratorFramework getCuratorFramework(String url) {
        if (ZOOKEEPER_ADDRESS.equals(url)) {
            return curatorFramework;
        }

        return getClient(url);
    }

    private static CuratorFramework getClient(String url){

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(url)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .sessionTimeoutMs(5000)
                .build();
        client.getConnectionStateListenable().addListener((CuratorFramework curatorFramework, ConnectionState connectionState) -> {
            if (ConnectionState.CONNECTED.equals(connectionState)) {
                System.out.println("与注册中心[" + ZOOKEEPER_ADDRESS + "]连接成功");
            }
        });
        client.start();

        return client;
    }

}
