package com.hanlinsir.concurrent.thread03_state;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 6:55 下午
 */
public class ThreadStateTest {

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println(Thread.currentThread().getName() + " is running...");
                    Thread.sleep(2000);
                    // 尝试唤醒 阻塞的线程
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"time_waiting-thread").start();

        // /WAITING，线程在 ThreadStatus 类锁上通过 wait 进行等待,并释放锁
        new Thread(() -> {
            while (true) {
                synchronized (ThreadStateTest.class) {
                    System.out.println(Thread.currentThread().getName() +  " is waiting ...");
                    try {
                        ThreadStateTest.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() +  " is running ...");
                }
            }
        },"waiting-thread").start();

        new Thread(() -> {
            while (true) {
                synchronized (ThreadStateTest.class) {
                    System.out.println(Thread.currentThread().getName() +  "is notifying ...");
                    ThreadStateTest.class.notifyAll();
                    System.out.println(Thread.currentThread().getName() +  "is sleeping ...");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"notify-thread").start();

        // sleep 不会释放锁
        new Thread(new BlockDemo(),"BlockThread-01").start();
        new Thread(new BlockDemo(),"BlockThread-02").start();
    }
}
