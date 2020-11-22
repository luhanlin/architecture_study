package com.hanlinsir.concurrent.thread04_interrupt;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 8:25 下午
 */
public class InterruptDemo {

    private static int count;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            // 默认为 false
            while (!Thread.currentThread().isInterrupted()) {
                count++;
                System.out.println(count);
            }
        }, "interrupt-thread");

        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
