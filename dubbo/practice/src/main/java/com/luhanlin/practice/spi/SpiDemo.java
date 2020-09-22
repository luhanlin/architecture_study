package com.luhanlin.practice.spi;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.Protocol;
import org.apache.dubbo.common.compiler.Compiler;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-15 10:39]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SpiDemo {

    public static void main(String[] args) {
        Protocol protocol= ExtensionLoader.getExtensionLoader(Protocol.class).getExtension("myprotocol");
        System.out.println(protocol.getDefaultPort());

        Compiler compiler=ExtensionLoader.getExtensionLoader(Compiler.class).getAdaptiveExtension();
        //.AdaptiveCompiler@1a93a7ca
    }
}
