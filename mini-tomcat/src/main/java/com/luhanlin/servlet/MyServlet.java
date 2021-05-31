package com.luhanlin.servlet;

import com.luhanlin.entity.Request;
import com.luhanlin.entity.Response;
import com.luhanlin.util.HttpProtocolUtil;

import java.io.IOException;

/**
 * HttpServlet
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class MyServlet extends HttpServlet{
    public void doGet(Request request, Response response) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String content = "<h1>TestServlet get</h1>";
        try {
            response.output((HttpProtocolUtil.getHttpHeader200(content.getBytes().length) + content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doPost(Request request, Response response) {
        String content = "<h1>TestServlet post</h1>";
        try {
            response.output((HttpProtocolUtil.getHttpHeader200(content.getBytes().length) + content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws Exception {

    }

    public void destory() throws Exception {

    }
}
