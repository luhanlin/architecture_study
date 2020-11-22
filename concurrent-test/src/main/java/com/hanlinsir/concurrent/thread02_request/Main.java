package com.hanlinsir.concurrent.thread02_request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 5:05 下午
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        SaveProcessor saveProcessor = new SaveProcessor();
        PrintProcessor printProcessor = new PrintProcessor(saveProcessor);
        saveProcessor.start();
        printProcessor.start();

        for (int i = 0; i < 100; i++) {
            Request request = new Request("request" + i);
            printProcessor.processRequest(request);
        }

        Thread.sleep(10000);
    }
}
