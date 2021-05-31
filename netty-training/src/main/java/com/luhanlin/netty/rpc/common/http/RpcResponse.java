package com.luhanlin.netty.rpc.common.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应包装类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcResponse {

    private String requestId;  // 响应id

    private Object result;      // 返回结果

    private String error;       // 错误信息
}
