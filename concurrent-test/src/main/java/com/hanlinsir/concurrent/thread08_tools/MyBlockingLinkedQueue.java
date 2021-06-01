package com.hanlinsir.concurrent.thread08_tools;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-11-23 15:52]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MyBlockingLinkedQueue {

    private Queue<String> queue = new LinkedList<>();

    private static final int MAX_COUNT = 100;

    private ReentrantLock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    public void add(String s) {
        lock.lock();
        try {
            if (queue.size() > MAX_COUNT) {
                System.out.println("queue is full ..");
                condition.await();
            }
            queue.add(s);
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String take() {
        lock.lock();
        try {
            if (queue.isEmpty()) {
                System.out.println("queue is empty ");
                condition.await();
            }
            queue.poll();
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();

        }
        return null;
    }

    public static void main(String[] args) {
        MyBlockingLinkedQueue queue = new MyBlockingLinkedQueue();

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                while (true) {
                    queue.add("data" + 1);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                while (true) {
                    queue.take();
                    try {
                        Thread.sleep(9);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
