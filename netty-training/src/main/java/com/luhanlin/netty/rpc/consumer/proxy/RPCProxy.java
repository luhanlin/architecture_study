package com.luhanlin.netty.rpc.consumer.proxy;

import com.luhanlin.netty.rpc.common.entity.Test;
import com.luhanlin.netty.rpc.protocol.InvokerProtocol;
import com.luhanlin.netty.rpc.serializer.JSONSerializer;
import com.luhanlin.netty.rpc.serializer.RpcDecoder;
import com.luhanlin.netty.rpc.serializer.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <类详细描述>
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
    public static <T> T create(Class<T> cls) {
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
                return rpcInvoke(proxy,method, args);
            }
        }

        private Object rpcInvoke(Object proxy, Method method, Object[] args) {
            //传输协议封装
            InvokerProtocol msg = new InvokerProtocol();
            msg.setClassName(this.cls.getName());
            msg.setMethodName(method.getName());
            msg.setValues(args);
            msg.setParamTypes(method.getParameterTypes());

            final RpcProxyHandler consumerHandler = new RpcProxyHandler();
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                //自定义协议解码器
                                /** 入参有5个，分别解释如下
                                 maxFrameLength：框架的最大长度。如果帧的长度大于此值，则将抛出TooLongFrameException。
                                 lengthFieldOffset：长度字段的偏移量：即对应的长度字段在整个消息数据中得位置
                                 lengthFieldLength：长度字段的长度：如：长度字段是int型表示，那么这个值就是4（long型就是8）
                                 lengthAdjustment：要添加到长度字段值的补偿值
                                 initialBytesToStrip：从解码帧中去除的第一个字节数
                                 */
                                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                                //自定义协议编码器
                                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                                //对象参数类型编码器
                                //pipeline.addLast("encoder", new ObjectEncoder());
                                pipeline.addLast("encoder", new RpcEncoder(InvokerProtocol.class, new JSONSerializer()));
                                //对象参数类型解码器
                                pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                                pipeline.addLast("handler",consumerHandler);
                            }
                        });

                ChannelFuture future = b.connect("localhost", 8081).sync();
                future.channel().writeAndFlush(msg).sync();
                future.channel().closeFuture().sync();
            } catch(Exception e){
                e.printStackTrace();
            }finally {
                group.shutdownGracefully();
            }
            return consumerHandler.getResponse();
        }
    }
}
