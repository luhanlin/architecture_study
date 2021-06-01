package com.hanlinsir.concurrent.thread01_create;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 类详细描述：通过显示 Callable 接口创建线程
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 4:00 下午
 */
public class MyCallableThread implements Callable<String> {

    @Override
    public String call() throws Exception {
        Thread.sleep(5000);
        return "CallableThread`s response ...";
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        MyCallableThread thread = new MyCallableThread();
        Future<String> future = executorService.submit(thread);

        System.out.println(future.get());

        executorService.shutdown();
    }
}
