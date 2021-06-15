package com.luhanlin.util;

import javax.servlet.ServletRequest;

/**
 * 对{@link javax.servlet.ServletRequest}数据进行保存到当前线程中，可以再web环境下获取
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class WebRequestHolder {

    private static ThreadLocal<ServletRequest> REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    public static ServletRequest getRequest() {
        return REQUEST_THREAD_LOCAL.get();
    }

    public static void set(ServletRequest request) {
        REQUEST_THREAD_LOCAL.set(request);
    }

    public static void remove() {
        REQUEST_THREAD_LOCAL.remove();
    }
}
