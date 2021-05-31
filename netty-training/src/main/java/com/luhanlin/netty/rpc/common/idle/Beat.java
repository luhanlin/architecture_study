package com.luhanlin.netty.rpc.common.idle;

import com.luhanlin.netty.rpc.common.http.RpcRequest;

/**
 * 心跳信息类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class Beat {

    public static final String BEAT_ID = "BEAT_PING_PONG";
    // 心跳默认发送间隔
    public static final int BEAT_INTERVAL = 3;

    public static RpcRequest BEAT_PING;

    static {
        BEAT_PING = RpcRequest.builder().requestId(BEAT_ID).build();
    }
}
