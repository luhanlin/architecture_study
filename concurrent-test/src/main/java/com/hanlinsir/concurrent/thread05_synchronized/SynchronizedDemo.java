package com.hanlinsir.concurrent.thread05_synchronized;

/**
 * 类详细描述：简易演示锁保证共享数据的安全性
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 10:32 下午
 */
public class SynchronizedDemo {

    private static int count = 0;

    public static void inc() {
        synchronized (SynchronizedDemo.class) {
            count++;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException  {
        for (int i = 0; i < 1000; i++) {
            new Thread(()-> SynchronizedDemo.inc()).start();
        }

        Thread.sleep(2000);

        System.out.println("count = " + count); //count = 1000
    }
}
