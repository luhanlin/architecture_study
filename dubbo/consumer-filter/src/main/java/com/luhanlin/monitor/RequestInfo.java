package com.luhanlin.monitor;

/**
 * 请求耗时时间统计
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class RequestInfo {

    private  String name;
    private  long   times;
    private  long   endTimes;

    public RequestInfo() {
    }

    public RequestInfo(String name, long times, long endTimes) {
        this.name = name;
        this.times = times;
        this.endTimes = endTimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public long getEndTimes() {
        return endTimes;
    }

    public void setEndTimes(long endTimes) {
        this.endTimes = endTimes;
    }
}
