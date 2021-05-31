package com.luhanlin.netty.rpc.consumer.handler;

import com.luhanlin.netty.rpc.common.http.RpcResponse;
import com.luhanlin.netty.rpc.common.idle.Beat;
import com.luhanlin.netty.rpc.common.idle.RequestMetrics;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * RPC 客户端处理服务器响应
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {
        RequestMetrics.getInstance().calculate(rpcResponse.getRequestId());
        System.out.println("请求id:" + rpcResponse.getRequestId() + ", 返回结果:" + rpcResponse.getResult());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.writeAndFlush(Beat.BEAT_PING);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
