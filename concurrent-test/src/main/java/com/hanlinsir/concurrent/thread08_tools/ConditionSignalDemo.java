package com.hanlinsir.concurrent.thread08_tools;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-11-23 15:16]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ConditionSignalDemo implements Runnable{

    private ReentrantLock lock;
    private Condition condition;

    public ConditionSignalDemo(ReentrantLock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        System.out.println("ConditionSignalDemo begin ...");
        try {
            lock.lock();
            condition.signal();
            System.out.println("ConditionSignalDemo end ...");
        } finally {
            lock.unlock();
        }
    }
}
