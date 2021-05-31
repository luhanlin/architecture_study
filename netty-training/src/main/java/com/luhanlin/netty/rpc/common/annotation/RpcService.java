package com.luhanlin.netty.rpc.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识被暴露的远程接口类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

    /**
     * 服务版本号，待实现
     *
     * @return
     */
    String version() default "";

    /**
     * 超时时间，待实现
     *
     * @return
     */
    int timeout() default -1;

}
