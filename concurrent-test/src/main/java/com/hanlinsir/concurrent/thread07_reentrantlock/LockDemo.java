package com.hanlinsir.concurrent.thread07_reentrantlock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-11-23 10:43]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LockDemo {

    static Map<String,Object> cache = new HashMap<>();

    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    static Lock readLock = rwl.readLock();

    static Lock writeLock = rwl.writeLock();

    public static Object get(String key) {
        readLock.lock();
        try {
            return cache.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public static void put(String key, Object obj){
        writeLock.lock();
        try {
            cache.put(key,obj);
        } finally {
            writeLock.unlock();
        }
    }
}
