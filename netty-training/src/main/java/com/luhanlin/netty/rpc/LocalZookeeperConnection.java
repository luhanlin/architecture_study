package com.luhanlin.netty.rpc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
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
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZOOKEEPER_ADDRESS)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();
    }

    public static CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }

}
