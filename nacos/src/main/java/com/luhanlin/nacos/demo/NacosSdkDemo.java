package com.luhanlin.nacos.demo;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;

public class NacosSdkDemo {

    public static void main(String[] args) throws IOException {
        //连接到目标服务的地址
        //指定dataid、 groupid
        String serverAddr="localhost:8848";
        String dataId="example";
        String groupId="DEFAULT_GROUP";
        Properties properties=new Properties();
        properties.put("serverAddr",serverAddr);
        try {
            //ConfigService-> NacosConfigService
            ConfigService configService=NacosFactory.createConfigService(properties);
            String content=configService.getConfig(dataId,groupId,3000);
            System.out.println(content);
            configService.addListener(dataId, groupId, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }
                @Override
                public void receiveConfigInfo(String configInfo) {
                    System.out.println("configInfo:"+configInfo);
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
//        System.in.read();
    }
}
