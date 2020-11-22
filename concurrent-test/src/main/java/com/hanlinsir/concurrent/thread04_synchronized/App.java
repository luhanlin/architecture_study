package com.hanlinsir.concurrent.thread04_synchronized;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/22 12:49 上午
 */
public class App {

    public synchronized static void test() {
        System.out.println("test...");
    }

    public static void main(String[] args) {
        synchronized (App.class) {
        }
        // 静态方法中锁为类锁
        test();
    }
}
