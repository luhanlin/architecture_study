package com.luhanlin.netty.rpc.register.handler;

import com.luhanlin.netty.rpc.common.listener.NodeChangeListener;

import java.util.List;

/**
 * 注册中心，用来做服务注册、服务发现
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public interface RpcRegistryHandler {

    /**
     * 服务注册
     *
     * @param service
     * @param ip
     * @param port
     * @return
     */
    boolean register(String service, String ip, int port);

    /**
     * 服务发现
     * @param serviceName
     * @return
     */
    List<String> discovery(String serviceName);

    /**
     * 添加监听器
     * @param listener
     */
    void addListener(NodeChangeListener listener);

    /**
     * 注册中心销毁
     */
    void destroy();

}
