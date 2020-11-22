package com.hanlinsir.concurrent.thread01_create;

/**
 * 类详细描述：通过实现 Runnable 接口创建线程
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 3:53 下午
 */
public class MyRunnableThread implements Runnable {

    @Override
    public void run() {
        System.out.println("This is a thread with Runnable ... ");
    }

    public static void main(String[] args) {
        new Thread(new MyRunnableThread()).start();
    }
}
