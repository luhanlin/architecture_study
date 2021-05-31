package com.luhanlin.netty.rpc.provider.loader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载服务信息
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class ProviderLoader {

    // 用来存放实现类的类名
    private static List<String> providerClassList = new ArrayList<>();

    // 用来装服务提供者的实例
    private static Map<String, Object> instanceCacheMap = new ConcurrentHashMap<>();

    static {
        /* 加载服务实例 */
        loadProviderInstance("com.luhanlin.netty.rpc");
    }

    private static void loadProviderInstance(String packageName) {
        // 完成递归扫描
        scannerClass(packageName);
        // 简单的保存接口信息
        doRegister(packageName + ".api");
    }

    private static void scannerClass(String packageName) {
        // 静态方法中，使用内部类来进行解决
        URL url = new Object() {
            public URL getPath() {
                String packageDir = packageName.replace(".", "/");
                URL o = this.getClass().getClassLoader().getResource(packageDir);
                return o;
            }
        }.getPath();

        File dir = new File(url.getFile());
        File[] fileArr = dir.listFiles();
        for (File file : fileArr) {
            if (file.isDirectory()) {
                scannerClass(packageName + "." + file.getName());
            } else {
                providerClassList.add(packageName + "." + file.getName().replace(".class", ""));
            }
        }
    }

    private static void doRegister(String packageName) {
        for (String className : providerClassList) {
            try {
                Class<?> providerClass = Class.forName(className);
                Class<?>[] interfaces = providerClass.getInterfaces();

                if (interfaces == null || interfaces.length <= 0) {
                    continue;
                }

                for (Class<?> inter : interfaces) {
                    String interName = inter.getName();
                    if (interName.startsWith(packageName)) {
                        instanceCacheMap.put(interName, providerClass.newInstance());
                        System.out.println("注册了" + interName + "的服务");
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, Object> getInstanceCacheMap() {
        return instanceCacheMap;
    }
}
