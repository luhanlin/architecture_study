package com.luhanlin.curator.watcher;

import com.luhanlin.curator.LocalZookeeperConnection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;

import java.util.List;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-08 16:58]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class WatcherOperator {

    private final CuratorFramework curatorFramework;

    public WatcherOperator() {
        this.curatorFramework = LocalZookeeperConnection.getCuratorFramework();
    }

    public void registerNodeCacheListener(String path) {
        try {
            NodeCache nodeCache = new NodeCache(curatorFramework, path, false);

            nodeCache.getListenable().addListener(()-> {
                System.out.println("receive node changed ... ");
                ChildData childData = nodeCache.getCurrentData();
                if(childData != null){
                    System.out.println("Path: " + childData.getPath());
                    System.out.println("Stat:" + childData.getStat());
                    System.out.println("Data: "+ new String(childData.getData()));
                }
            });
            nodeCache.start();
        } catch (Exception e) {
            System.out.println("注册 NodeCache 监听器出现异常，" + e.getMessage());
        }
    }

    public void registerPathChildCacheListener(String path) {
        try {
            PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, path, false);

            childrenCache.getListenable().addListener((client, event)-> {
                System.out.println("receive path child changed ... ");
                System.out.println("listener:"+ event);
            });
            childrenCache.start();
        } catch (Exception e) {
            System.out.println("注册 PathChildCache 监听器出现异常，" + e.getMessage());
        }
    }

    public void registerTreeCacheListener(String path) {
        try {
            TreeCache treeCache = new TreeCache(curatorFramework, path);

            treeCache.getListenable().addListener((client, event)-> {
                System.out.println("receive tree cache changed ... ");
                System.out.println("listener:"+ event);
            });
            treeCache.start();
        } catch (Exception e) {
            System.out.println("注册 tree cache 监听器出现异常，" + e.getMessage());
        }
    }
}
