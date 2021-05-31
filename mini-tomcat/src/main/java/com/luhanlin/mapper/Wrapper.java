package com.luhanlin.mapper;

import com.luhanlin.servlet.Servlet;

/**
 * url 与 servlet 的映射
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class Wrapper {

    private String urlPattern;
    private Servlet servlet;

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

}
