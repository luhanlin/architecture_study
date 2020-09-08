package com.luhanlin.curator;

import com.luhanlin.curator.operator.CuratorClientOperator;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-09-08 14:46]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CuratorDemo {

    public static void main(String[] args) throws Exception {
        String path = "/test/55";
        CuratorClientOperator curatorClientOperator = new CuratorClientOperator();
        curatorClientOperator.curatorStart();
        curatorClientOperator.create(path, "add");
//        curatorClientOperator.createWithACL(path, "add");
        curatorClientOperator.get(path);
        curatorClientOperator.update(path, "update");
        curatorClientOperator.delete("/test");
    }

}
