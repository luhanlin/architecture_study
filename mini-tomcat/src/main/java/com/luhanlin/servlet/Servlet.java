package com.luhanlin.servlet;

import com.luhanlin.entity.Request;
import com.luhanlin.entity.Response;

/**
 * servlet 接口
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public interface Servlet {

    void init() throws Exception;

    void destory() throws Exception;

    void service(Request request, Response response) throws Exception;
}
