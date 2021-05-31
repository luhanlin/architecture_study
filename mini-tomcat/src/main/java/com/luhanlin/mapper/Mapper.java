package com.luhanlin.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 映射类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class Mapper {

    private List<Host> hosts = new ArrayList<>();

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }

}
