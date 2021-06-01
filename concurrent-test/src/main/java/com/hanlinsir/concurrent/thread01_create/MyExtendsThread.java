package com.hanlinsir.concurrent.thread01_create;

/**
 * 类详细描述：继承 Thread 创建线程
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 3:40 下午
 */
public class MyExtendsThread extends Thread {

    @Override
    public void run() {
        System.out.println("This is a thread with extending...");
    }

    public static void main(String[] args) {
        new MyExtendsThread().start();
    }
}
