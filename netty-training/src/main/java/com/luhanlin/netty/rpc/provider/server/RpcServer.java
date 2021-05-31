package com.luhanlin.netty.rpc.provider.server;

import com.luhanlin.netty.rpc.common.coder.RpcDecoder;
import com.luhanlin.netty.rpc.common.coder.RpcEncoder;
import com.luhanlin.netty.rpc.common.config.KeeperConfig;
import com.luhanlin.netty.rpc.common.http.RpcRequest;
import com.luhanlin.netty.rpc.common.http.RpcResponse;
import com.luhanlin.netty.rpc.common.idle.Beat;
import com.luhanlin.netty.rpc.common.serialize.impl.JSONSerializer;
import com.luhanlin.netty.rpc.provider.handler.RpcServiceHandler;
import com.luhanlin.netty.rpc.provider.loader.ProviderLoader;
import com.luhanlin.netty.rpc.provider.server.config.RpcServerConfig;
import com.luhanlin.netty.rpc.register.RpcRegistryFactory;
import com.luhanlin.netty.rpc.register.handler.RpcRegistryHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务启动类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Service
public class RpcServer implements InitializingBean, DisposableBean {

    private static final String IP = "127.0.0.1";

    @Autowired
    private RpcRegistryFactory registryFactory;

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private RpcServerConfig config;

    private void startServer() throws Exception {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
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
                            // 请求参数解码
                            .addLast(new RpcDecoder(RpcRequest.class, new JSONSerializer()))
                            // 响应编码
                            .addLast(new RpcEncoder(RpcResponse.class, new JSONSerializer()))
                            // 心跳检测
                            .addLast(new IdleStateHandler(0, 0, Beat.BEAT_INTERVAL * 3, TimeUnit.SECONDS))
                            // 进行编解码后的处理
                            .addLast(new RpcServiceHandler());
                    }
                });

        config.setIp(IP);

        ChannelFuture future = server.bind(config.getPort()).sync();
        // 延迟注册
        if (config.getDelay() > 0) {
            Thread.sleep(config.getDelay());
        }

        System.out.println("=============开始注册=============");
        this.registry(config);
        System.out.println("=============启动成功, ip:" + IP + ", port:" + config.getPort() + "=============");
        future.channel().closeFuture().sync();
    }

    private void registry(RpcServerConfig config) throws Exception {
        Map<String, Object> services = config.getServices();

        if (services.isEmpty()) {
            System.out.println("no service find");
            throw new RuntimeException("no service find");
        }

        RpcRegistryHandler registryHandler = registryFactory.getObject();
        if (registryHandler == null) {
            System.out.println("registryHandler not fund !");
            throw new RuntimeException("registryHandler not fund !");
        }

        services.keySet().forEach(interfaceName -> registryHandler.register(interfaceName, config.getIp(), config.getPort()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> instanceCacheMap = ProviderLoader.getInstanceCacheMap();

        config = RpcServerConfig.builder()
                .applicationName("rpc-provider")
                .port(KeeperConfig.getInstance().getPort())
                .delay(3000)
                .services(instanceCacheMap)
                .providerSide(true)
                .build();
        startServer();
    }

    @Override
    public void destroy() throws Exception {
        if (null != bossGroup) {
            bossGroup.shutdownGracefully();
        }
        if (null != workerGroup) {
            workerGroup.shutdownGracefully();
        }
    }
}
