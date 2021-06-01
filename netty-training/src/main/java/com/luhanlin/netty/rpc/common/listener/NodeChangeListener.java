package com.luhanlin.netty.rpc.common.listener;

import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import java.util.List;

/**
 * 节点变更监听
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public interface NodeChangeListener {

    /**
     * 节点变更时，触发listeners
     *
     * @param children
     * @param serviceList
     * @param pathChildrenCacheEvent
     */
    void notify(String children, List<String> serviceList, TreeCacheEvent treeCacheEvent);

}
