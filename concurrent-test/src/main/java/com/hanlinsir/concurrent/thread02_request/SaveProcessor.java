package com.hanlinsir.concurrent.thread02_request;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 5:04 下午
 */
public class SaveProcessor extends Thread implements RequestProcessor{

    private LinkedBlockingQueue<Request> queue = new LinkedBlockingQueue();

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
                System.out.println("save data :" + request.getName() + "over ... ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
