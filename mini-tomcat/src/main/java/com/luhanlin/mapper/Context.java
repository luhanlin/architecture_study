package com.luhanlin.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 *  上下文，对path 和servlet wrapper 的封装
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class Context {

    private String path;

    private List<Wrapper> wrappers = new ArrayList<>();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Wrapper> getWrappers() {
        return wrappers;
    }

    public void setWrappers(List<Wrapper> wrappers) {
        this.wrappers = wrappers;
    }
}
