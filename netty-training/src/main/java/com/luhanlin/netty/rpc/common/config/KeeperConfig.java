package com.luhanlin.netty.rpc.common.config;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 全局配置文件
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class KeeperConfig {

    private static final AtomicReference<KeeperConfig> INSTANCE = new AtomicReference<>();

    // 端口号
    private int port;
    // zookeeper地址
    private String zkAddr;
    // 主动上报间隔时间，单位:秒
    private int interval;
    // 客户端侧
    private boolean consumerSide;
    // 服务端侧
    private boolean providerSide;

    private KeeperConfig() {

    }

    /**
     * 全局单例
     */
    public static KeeperConfig getInstance() {
        for (;;) {
            KeeperConfig keeperConfig = INSTANCE.get();

            if (keeperConfig != null) {
                return keeperConfig;
            }
            keeperConfig = new KeeperConfig();

            if (INSTANCE.compareAndSet(null, keeperConfig)) {
                return keeperConfig;
            }
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getZkAddr() {
        return zkAddr;
    }

    public void setZkAddr(String zkAddr) {
        this.zkAddr = zkAddr;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isConsumerSide() {
        return consumerSide;
    }

    public void setConsumerSide(boolean consumerSide) {
        this.consumerSide = consumerSide;
        this.providerSide = !consumerSide;
    }

    public boolean isProviderSide() {
        return providerSide;
    }

    public void setProviderSide(boolean providerSide) {
        this.providerSide = providerSide;
        this.consumerSide = !providerSide;
    }
}
