package com.luhanlin.filter;

import com.luhanlin.util.WebRequestHolder;

import javax.servlet.*;
import java.io.IOException;

/**
 * 将请求存储到当前线程中
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class ServletRequestIpFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 存储request 到当前线程中，传递到下游
        try {
            WebRequestHolder.set(servletRequest);
        } finally {
            // 请求完成后需要释放request
            WebRequestHolder.remove();
        }
    }

    @Override
    public void destroy() {

    }
}
