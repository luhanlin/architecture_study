package com.hanlinsir.concurrent.thread02_request;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 4:59 下午
 */
public class PrintProcessor extends Thread implements RequestProcessor{

    private LinkedBlockingQueue<Request> queue = new LinkedBlockingQueue();

    private RequestProcessor processor;

    public PrintProcessor(RequestProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void processRequest(Request req) {
        queue.add(req);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Request request = queue.take();
                Thread.sleep(100);
                System.out.println("print data :" + request.getName());
                processor.processRequest(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
