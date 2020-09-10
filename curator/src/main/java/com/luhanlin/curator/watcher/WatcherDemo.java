package com.luhanlin.curator.watcher;

import com.luhanlin.curator.LocalZookeeperConnection;
import com.luhanlin.curator.operator.CuratorClientOperator;

import java.io.IOException;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-08 16:49]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class WatcherDemo {

    public static void main(String[] args) throws IOException {
        String path = "/watch";
        LocalZookeeperConnection.getCuratorFramework().start();
        WatcherOperator watcher = new WatcherOperator();
        watcher.registerNodeCacheListener(path);
        watcher.registerPathChildCacheListener(path);
        watcher.registerTreeCacheListener(path);

        System.in.read();
    }


}
