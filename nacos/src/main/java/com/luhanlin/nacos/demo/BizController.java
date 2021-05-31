package com.luhanlin.nacos.demo;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;

@NacosPropertySource(dataId = "biz",groupId = "DEFAULT_GROUP",autoRefreshed = true)
public class BizController {

}
