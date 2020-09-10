package com.luhanlin.netty.rpc.register;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-03 14:46]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RPCRegister {

    private static int port;

    public RPCRegister(int port) {
        this.port = port;
    }

    public static int getPort() {
        return port;
    }

    private void start() {
        NioEventLoopGroup selector = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(selector, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    // 自定义协议解码器
                                    /** 入参有5个，分别解释如下
                                     maxFrameLength：框架的最大长度。如果帧的长度大于此值，则将抛出TooLongFrameException。
                                     lengthFieldOffset：长度字段的偏移量：即对应的长度字段在整个消息数据中得位置
                                     lengthFieldLength：长度字段的长度。如：长度字段是int型表示，那么这个值就是4（long型就是8）
                                     lengthAdjustment：要添加到长度字段值的补偿值
                                     initialBytesToStrip：从解码帧中去除的第一个字节数
                                     */
                                    .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                    // 自定义协议编码器
                                    .addLast(new LengthFieldPrepender(4))
                                    // 对象参数类型编码器
                                    .addLast("encoder",new ObjectEncoder())
                                    // 对象参数类型解码器
                                    .addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                    // 进行编解码后的处理
                                    .addLast(new RegisterHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = server.bind(port).sync();
            System.out.println("GP RPC Registry start listen at " + port );
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            selector.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new RPCRegister(8081).start();
    }
}
