package com.luhanlin.netty.rpc.register;

import com.luhanlin.netty.rpc.common.config.KeeperConfig;
import com.luhanlin.netty.rpc.register.handler.RpcRegistryHandler;
import com.luhanlin.netty.rpc.register.handler.impl.ZookeeperRegistryHandler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Service;

/**
 * 注册中心工厂类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Service
public class RpcRegistryFactory implements FactoryBean<RpcRegistryHandler>, DisposableBean {

    private RpcRegistryHandler rpcRegistryHandler;

    @Override
    public RpcRegistryHandler getObject() throws Exception {
        if (rpcRegistryHandler != null) {
            return rpcRegistryHandler;
        }

        // 默认使用zk作为默认注册中心
        rpcRegistryHandler = new ZookeeperRegistryHandler(KeeperConfig.getInstance().getZkAddr());
        return rpcRegistryHandler;
    }

    @Override
    public Class<?> getObjectType() {
        return RpcRegistryHandler.class;
    }

    @Override
    public void destroy() throws Exception {
        if (rpcRegistryHandler != null) {
            rpcRegistryHandler.destroy();
        }
    }
}
