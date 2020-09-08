package com.luhanlin.curator.operator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-08 14:59]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CuratorClientOperator {

    private static final String ZOOKEEPER_ADDRESS = "127.0.0.1:2181";

    private final CuratorFramework curatorFramework;

    public CuratorClientOperator() {
        this.curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZOOKEEPER_ADDRESS)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
    }

    public void create(String path, String val) throws Exception {
        curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, val.getBytes());
    }

    public void createWithACL(String path, String val) throws Exception {
        curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.READ_ACL_UNSAFE)
                .forPath(path, val.getBytes());
    }

    public void update(String path, String val) throws Exception {
        curatorFramework.setData()
                .withVersion(0)
                .forPath(path, val.getBytes());
    }

    public void delete(String path) throws Exception {
        curatorFramework.delete()
                .guaranteed()                   // 如果删除失败，那么在后端还是继续会删除，直到成功
                .deletingChildrenIfNeeded()     // 如果有子节点，就删除
                .withVersion(0)
                .forPath(path);
    }

    public int get(String path) throws Exception {
        Stat stat = new Stat();
        byte[] data = curatorFramework.getData()
                .storingStatIn(stat)
                .forPath(path);
        System.out.println("节点" + path + "的数据为: " + new String(data));
        System.out.println("该节点的版本号为: " + stat.getVersion());

        return stat.getVersion();
    }


    public void curatorStart(){
        curatorFramework.start();
        boolean isZkCuratorStarted = curatorFramework.getState() == CuratorFrameworkState.STARTED;
        System.out.println("当前客户的状态：" + (isZkCuratorStarted ? "连接中" : "已关闭"));
    }

}
