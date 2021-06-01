package com.luhanlin.netty.rpc.consumer.client;

import com.luhanlin.netty.rpc.common.coder.RpcDecoder;
import com.luhanlin.netty.rpc.common.coder.RpcEncoder;
import com.luhanlin.netty.rpc.common.http.RpcRequest;
import com.luhanlin.netty.rpc.common.http.RpcResponse;
import com.luhanlin.netty.rpc.common.idle.Beat;
import com.luhanlin.netty.rpc.monitor.metrics.RequestMetrics;
import com.luhanlin.netty.rpc.common.serialize.impl.JSONSerializer;
import com.luhanlin.netty.rpc.consumer.handler.RpcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Rpc Client 端
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Data
public class RpcClient {

    private String ip;
    private int port;
    private String serviceName;

    private EventLoopGroup clientGroup;
    private Channel channel;

    public RpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public RpcClient(String ip, int port, String serviceName) {
        this.ip = ip;
        this.port = port;
        this.serviceName = serviceName;
    }

    public void initClient() throws InterruptedException {
        this.clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE,true)
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new IdleStateHandler(0, 0, Beat.BEAT_INTERVAL, TimeUnit.SECONDS))
                                .addLast(new RpcEncoder(RpcRequest.class, new JSONSerializer()))
                                .addLast(new RpcDecoder(RpcResponse.class, new JSONSerializer()))
                                .addLast(new RpcClientHandler());
                    }
                });

        this.channel = bootstrap.connect(ip, port).sync().channel();

        // 判断连接是否有效
        if (!isValidate()) {
            close();
            return;
        }
        System.out.println("====启动客户端：" + serviceName + ", ip:" + ip + ", port:" + port + "=====");
    }

    private boolean isValidate() {
        if (this.channel != null) {
            return this.channel.isActive();
        }
        return false;
    }

    public void close() {
        if (this.channel != null && this.channel.isActive()) {
            this.channel.close();
        }
        if (this.clientGroup != null && !this.clientGroup.isShutdown()) {
            this.clientGroup.shutdownGracefully();
        }
        System.out.println("rpc client close");
    }

    public Object sent(RpcRequest rpcRequest) throws InterruptedException, ExecutionException {
        // 开始统计请求时间
        RequestMetrics.getInstance().put(ip, port, rpcRequest.getRequestId(), rpcRequest.getClassName());
        System.out.println("请求的服务器地址为：" + ip + ":" + port);
        if (channel.isWritable()) {
            return this.channel.writeAndFlush(rpcRequest).sync().get();
        }
        System.out.println(" TODO 服务被断开，可以重新路由... ");
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RpcClient rpcClient = (RpcClient) o;
        return port == rpcClient.port &&
                ip.equals(rpcClient.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
