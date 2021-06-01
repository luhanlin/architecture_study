package com.luhanlin.netty.rpc.provider.handler;

import com.luhanlin.netty.rpc.common.annotation.RpcService;
import com.luhanlin.netty.rpc.common.http.RpcRequest;
import com.luhanlin.netty.rpc.common.http.RpcResponse;
import com.luhanlin.netty.rpc.common.idle.Beat;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;

/**
 * 服务端请求处理
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Component
public class RpcServiceHandler extends SimpleChannelInboundHandler<RpcRequest> implements ApplicationContextAware {

    private static final Map<String, Object> SERVICE_INSTANCE_MAP = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        if (Beat.BEAT_ID.equalsIgnoreCase(rpcRequest.getRequestId())) {
            System.out.println("===idle===");
            return;
        }

        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());
        try {
            response.setResult(handler(rpcRequest));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        ctx.writeAndFlush(response);
    }

    private Object handler(RpcRequest request) throws InvocationTargetException {
        Object serviceBean = SERVICE_INSTANCE_MAP.get(request.getClassName());

        Class<?> serviceClass = serviceBean.getClass();

        String methodName = request.getMethodName();

        Class<?>[] parameterTypes = request.getParamTypes();
        Object[] parameters = request.getValues();

        //使用CGLB Reflect
        FastClass fastClass = FastClass.create(serviceClass);
        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);

        return fastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);

        if (!serviceBeanMap.isEmpty()) {
            Set<Map.Entry<String, Object>> entries = serviceBeanMap.entrySet();
            for (Map.Entry<String, Object> item : entries) {
                Object serviceBean = item.getValue();
                if (serviceBean.getClass().getInterfaces().length == 0) {
                    throw new RuntimeException("service must implements interface.");
                }
                String interfaceName = serviceBean.getClass().getInterfaces()[0].getName();
                SERVICE_INSTANCE_MAP.put(interfaceName, serviceBean);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("发生异常:" + cause.getMessage());
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            System.out.println("心跳通道关闭");
            ctx.channel().close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
