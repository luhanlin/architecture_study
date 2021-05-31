package com.luhanlin.servlet;

import com.luhanlin.entity.Request;
import com.luhanlin.entity.Response;

/**
 * HttpServlet 包装类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public abstract class HttpServlet implements Servlet{

    public abstract void doGet(Request request,Response response);

    public abstract void doPost(Request request,Response response);

    public void service(Request request, Response response) throws Exception {
        if("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request,response);
        }else{
            doPost(request,response);
        }
    }
}
