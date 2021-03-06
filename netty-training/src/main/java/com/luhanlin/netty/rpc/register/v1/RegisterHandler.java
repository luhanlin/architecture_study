package com.luhanlin.netty.rpc.register.v1;

import com.alibaba.fastjson.JSON;
import com.luhanlin.netty.rpc.common.utils.LocalZookeeperConnection;
import com.luhanlin.netty.rpc.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-03 15:02]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RegisterHandler extends ChannelInboundHandlerAdapter {
    // 用保存所有可用的服务
    public static ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<String,Object>();

    // 保存所有相关的服务类
    private List<String> classNames = new ArrayList<String>();

    public RegisterHandler() {
        // 完成递归扫描
        scannerClass("com.luhanlin.netty.rpc.provider");
        // 简单的保存接口信息
        doRegister();
    }

    public static Set<String> getServiceName(){
        return registryMap.keySet();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        InvokerProtocol request = (InvokerProtocol) msg;

        //当客户端建立连接时，需要从自定义协议中获取信息，拿到具体的服务和实参
        //使用反射调用
        if(registryMap.containsKey(request.getClassName())){
            Object clazz = registryMap.get(request.getClassName());
            Method method = clazz.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            result = method.invoke(clazz, request.getValues());
            System.out.println("服务端调用成功：" + result);
        }
        ctx.write(JSON.toJSONString(result));
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void scannerClass(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            //如果是一个文件夹，继续递归
            if(file.isDirectory()){
                scannerClass(packageName + "." + file.getName());
            }else{
                classNames.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    private void doRegister() {
        if(classNames.size() == 0){ return; }
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                Class<?> i = clazz.getInterfaces()[0];
                registryMap.put(i.getName(), clazz.newInstance());

                registerServiceWithZk(i.getName(), getAddress() + ":" + RPCRegister.getPort());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void registerServiceWithZk(String serviceName, String serviceAddress) {
        try {
            CuratorFramework curatorFramework = LocalZookeeperConnection.getCuratorFramework();

            String servicePath = "/" + serviceName;

            // 服务节点不存在
            if(curatorFramework.checkExists().forPath(servicePath) == null) {
                curatorFramework.create()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(servicePath);
                String serviceAddressPath = servicePath + "/" + serviceAddress;

                curatorFramework.create()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(serviceAddressPath);

                System.out.println(serviceName + " 服务注册成功...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAddress(){
        InetAddress address = null;

        try {
            address = address.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return address.getHostAddress();
    }
}
