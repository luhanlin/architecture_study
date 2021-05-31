package com.luhanlin.netty.rpc.provider.server.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Rpc服务端配置类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RpcServerConfig {

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 本机IP
     */
    private String ip;

    /**
     * 端口号
     */
    private int port;

    /**
     * 延迟暴露
     */
    private int delay;

    /**
     * 是否为提供者
     */
    private boolean providerSide;

    /**
     * 扫描的服务
     */
    private Map<String, Object> services;

    /**
     * 服务列表
     */
    private List<String> serviceList;

}
