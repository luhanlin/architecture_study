package com.luhanlin.netty.rpc.consumer.loadbalance.strategy;

import com.luhanlin.netty.rpc.consumer.client.RpcClient;
import com.luhanlin.netty.rpc.consumer.loadbalance.AbstractLoadBalance;
import com.luhanlin.netty.rpc.monitor.metrics.RequestMetrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 请求最小花费策略, 当只有一个客户端进行连接操作时，会出现倾斜
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class MinRequestCostStrategy extends AbstractLoadBalance {

    private List<RpcClient> rpcClients = new ArrayList<>();

    @Override
    public RpcClient doGetRpcClient(List<RpcClient> rpcClients) {
        this.rpcClients = rpcClients;
        Random random = new Random();

        // 获取耗时统计信息
        List<RequestMetrics.Metrics> metricsList = RequestMetrics.getInstance().getAllInstances();
        if (metricsList.isEmpty()) {
            return rpcClients.get(random.nextInt(rpcClients.size()));
        }

        // 不论可以选中多少个，一般排序后，第一个肯定为最小花费时间的
        Collections.sort(metricsList);
        RequestMetrics.Metrics metrics = metricsList.get(0);
        System.out.println("++服务[" + metrics.getIp() + ":" + metrics.getPort() + "]" + "上次处理请求最小耗时：" + metrics.getCost());

        if (metricsList.size() == 1) {
            System.out.println("---只有一条记录信息？-----" + metrics);
            return filterClient(metrics);
        }

        // 当有多个时，过滤出花费最小的几个
        List<RequestMetrics.Metrics> minCostMetrics = metricsList.stream().
                filter(metric -> metric.getCost() == metrics.getCost()).collect(Collectors.toList());

        // 随机选取一个
        RequestMetrics.Metrics metrics1 = minCostMetrics.get(random.nextInt(minCostMetrics.size()));
        System.out.println("--- 过滤最小值后，还剩余：" + minCostMetrics.size() + ";");
        RpcClient rpcClient = filterClient(metrics1);
        if (rpcClient == null) {
            return rpcClients.get(random.nextInt(rpcClients.size()));
        }
        System.out.println("=============通过耗时决策选取===============");
        return rpcClient;
    }

    private RpcClient filterClient(RequestMetrics.Metrics metrics) {
        return rpcClients.stream()
                .filter(rpcClient -> rpcClient.getIp().equals(metrics.getIp()) && rpcClient.getPort() == metrics.getPort())
                .collect(Collectors.toList()).get(0);
    }
}
