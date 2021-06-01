package com.hanlinsir.concurrent.thread06_volatile;

/**
 * 类详细描述：在不加 volatile 关键字时，线程并不会停止
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/22 9:09 上午
 */
public class VolatileDemo {

    private volatile static boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            long i = 0L;
            while (!flag) {
                i++;
            }
            System.out.println("count = " + i);
        }).start();

        System.out.println("Thread is running ... ");
        Thread.sleep(1000);
        flag = true;
    }
}
