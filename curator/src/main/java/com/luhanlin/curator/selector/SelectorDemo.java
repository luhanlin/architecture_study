package com.luhanlin.curator.selector;

import com.luhanlin.curator.LocalZookeeperConnection;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-10 10:04]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SelectorDemo {

    public static void main(String[] args) {
        CuratorFramework client = LocalZookeeperConnection.getCuratorFramework();

        final InterProcessMutex lock = new InterProcessMutex(client, "/lock");

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "正在尝试获取锁...");
                try {
                    lock.acquire();
                    System.out.println(Thread.currentThread().getName() + "获取了锁。");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
