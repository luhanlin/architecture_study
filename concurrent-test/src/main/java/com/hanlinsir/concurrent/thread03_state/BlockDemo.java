package com.hanlinsir.concurrent.thread03_state;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 6:54 下午
 */
public class BlockDemo extends Thread {

    @Override
    public void run() {
        while (true) {
            synchronized (BlockDemo.class) {
                try {
                    Thread.sleep(2000);
                    System.out.println(Thread.currentThread().getName() + " 执行完毕...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
