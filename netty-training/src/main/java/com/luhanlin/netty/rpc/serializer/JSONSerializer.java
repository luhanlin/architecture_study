package com.luhanlin.netty.rpc.serializer;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

/**
 * fastjson 实现
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class JSONSerializer implements Serializer {

    public byte[] serialize(Object object) throws IOException {
        return JSON.toJSONBytes(object);
    }

    public <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
        return JSON.parseObject(bytes, clazz);
    }
}
