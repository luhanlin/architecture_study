package com.luhanlin.netty.rpc.common.http;

import lombok.Builder;
import lombok.Data;

/**
 * 请求包装类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Data
@Builder
public class RpcRequest {

    private String requestId;   // 请求对象的id

    private String className;   //类名

    private String methodName;  //函数名称

    private Class<?>[] paramTypes;//形参列表

    private Object[] values;    //实参列表
}
