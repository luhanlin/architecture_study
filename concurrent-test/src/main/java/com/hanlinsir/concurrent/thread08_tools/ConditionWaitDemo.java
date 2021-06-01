package com.hanlinsir.concurrent.thread08_tools;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-11-23 15:14]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ConditionWaitDemo implements Runnable{

    private ReentrantLock lock;
    private Condition condition;

    public ConditionWaitDemo(ReentrantLock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        System.out.println("ConditionWaitDemo begin ...");
        try {
            lock.lock();
            condition.await();
            System.out.println("ConditionWaitDemo end ...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
