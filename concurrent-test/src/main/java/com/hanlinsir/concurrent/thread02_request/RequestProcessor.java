package com.hanlinsir.concurrent.thread02_request;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/11/21 4:57 下午
 */
public interface RequestProcessor {

    /**
     * 处理请求
     * @param req
     */
    void processRequest(Request req);
}
