package com.hanlinsir.concurrent.thread04_interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 8:55 下午
 */
public class InterruptDemo2 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Before status :" + Thread.currentThread().isInterrupted());
                    Thread.interrupted(); // 对线程进行复位
                    System.out.println("After status :" + Thread.currentThread().isInterrupted());
                }
            }
        }, "interrupt-thread2");

        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }
}
