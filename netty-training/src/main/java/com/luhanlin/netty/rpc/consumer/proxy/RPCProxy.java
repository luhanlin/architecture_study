package com.luhanlin.netty.rpc.consumer.proxy;

import com.luhanlin.netty.rpc.common.http.RpcRequest;
import com.luhanlin.netty.rpc.common.http.RpcResponse;
import com.luhanlin.netty.rpc.consumer.RPCConsumer;
import com.luhanlin.netty.rpc.consumer.client.RpcClient;
import com.luhanlin.netty.rpc.protocol.InvokerProtocol;
import com.luhanlin.netty.rpc.common.serialize.impl.JSONSerializer;
import com.luhanlin.netty.rpc.common.coder.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * <类详细描述> 客户端连接代理对象
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-03 15:44]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RPCProxy {

    /**
     * 创建代理对象
     * @param cls interface 对象
     * @param <T>
     * @return
     */
    public static <T> T create(final Class<T> cls) {
        Class[] interfaces = cls.isInterface() ? new Class[]{cls} : cls.getInterfaces();

        return (T)Proxy.newProxyInstance(cls.getClassLoader(), interfaces, new RPCInvocationHandler(cls));
    }

    private static class RPCInvocationHandler implements InvocationHandler {

        private Class<?> cls;

        public RPCInvocationHandler(Class<?> cls) {
            this.cls = cls;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 这里
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(proxy, args);
            } else {
                return rpcInvoke(proxy,method, args, cls);
            }
        }

        private Object rpcInvoke(Object proxy, Method method, Object[] args, Class<?> cls) throws InterruptedException, ExecutionException {
            //传输协议封装
            RpcRequest rpcRequest = RpcRequest.builder()
                    .requestId(UUID.randomUUID().toString())
                    .className(this.cls.getName())
                    .methodName(method.getName())
                    .paramTypes(method.getParameterTypes())
                    .values(args).build();

            System.out.println("请求的内容为：" + rpcRequest);

            // 去服务端获取数据
            List<RpcClient> rpcClients = RPCConsumer.CLIENT_POOL.get(cls.getName());

            if (rpcClients == null && rpcClients.isEmpty()) {
                return null;
            }

            RpcClient rpcClient = rpcClients.get(new Random().nextInt(rpcClients.size()));
            try {
                return rpcClient.sent(rpcRequest);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}
