package com.hanlinsir.concurrent.thread07_reentrantlock;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-11-23 10:29]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ReentrantDemo {

    public synchronized void fun1() {
        System.out.println("fun1");
        fun2();
    }

    public void fun2() {
        synchronized (this) {
            System.out.println("fun2");
        }
    }

    public static void main(String[] args) {
        ReentrantDemo demo = new ReentrantDemo();
        new Thread(demo::fun1).start();
    }
}
