package com.hanlinsir.concurrent.thread04_interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 9:14 下午
 */
public class InterruptDemo3 {

    private static int count;

    public static void main(String[] args) throws InterruptedException {
        Thread thread01 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                count++;
            }
            System.out.println("num1: " + count);
        }, "thread-1");

        Thread thread02 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println("thread02 is running");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("num2: " + count);
        }, "thread-2");

        thread01.start();
        thread02.start();

        TimeUnit.SECONDS.sleep(1);
        thread01.interrupt();
        thread02.interrupt();

        System.out.println("thread01 = " + thread01.isInterrupted());
        System.out.println("thread02 = " + thread02.isInterrupted());
    }
}
