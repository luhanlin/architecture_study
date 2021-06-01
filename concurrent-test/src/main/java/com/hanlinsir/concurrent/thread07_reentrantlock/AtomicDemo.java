package com.hanlinsir.concurrent.thread07_reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-11-23 10:35]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AtomicDemo {

    private static int count = 0;
    private static ReentrantLock lock = new ReentrantLock();

    public static void inc() {
        lock.lock();
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            new Thread(()-> AtomicDemo.inc()).start();
        }
        Thread.sleep(3000);
        System.out.println("count = " + count);
    }

}
