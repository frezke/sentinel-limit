package com.aibank.framework.sentinellimit.task;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.node.ClusterNode;
import com.alibaba.csp.sentinel.node.metric.MetricNode;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.clusterbuilder.ClusterBuilderSlot;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.text.SimpleDateFormat;
import java.util.*;


public class MetricPrintTask implements Runnable {
    @Override
    public void run() {
        Map<Long, List<MetricNode>> maps = new TreeMap<>();
        for (Map.Entry<ResourceWrapper, ClusterNode> e : ClusterBuilderSlot.getClusterNodeMap().entrySet()) {
            ClusterNode node = e.getValue();
            Map<Long, MetricNode> metrics = node.metrics();
            aggregate(maps, metrics, node);
        }
        aggregate(maps, Constants.ENTRY_NODE.metrics(), Constants.ENTRY_NODE);
        if (!maps.isEmpty()) {
            for (Map.Entry<Long, List<MetricNode>> entry : maps.entrySet()) {
                try {
                    //TODO  打印到 业务日志文件, 硬件数据
                    for (MetricNode metricNode : entry.getValue()) {
                     //   RecordLog.warn(metricNode.toFatString());
                        HashMap<String, Object> metricLog = new HashMap<>();
                        metricLog.put("timestamp", metricNode.getTimestamp());
                        metricLog.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(metricNode.getTimestamp())));
                        metricLog.put("resource",metricNode.getResource());
                        metricLog.put("passQps",metricNode.getPassQps());
                        metricLog.put("blockQps",metricNode.getBlockQps());
                        metricLog.put("successQps",metricNode.getSuccessQps());
                        metricLog.put("exceptionQps",metricNode.getExceptionQps());
                        metricLog.put("Rt",metricNode.getRt());
                        metricLog.put("OccupiedPassQps", metricNode.getOccupiedPassQps());
                        metricLog.put( "concurrency",metricNode.getConcurrency());
                        metricLog.put("classification",metricNode.getClassification());
                        metricLog.put("currentCpuUsage",SystemRuleManager.getCurrentCpuUsage());
                        metricLog.put("cpuUsageThreshold",SystemRuleManager.getCpuUsageThreshold());
                        metricLog.put("currentSystemAvgLoad",SystemRuleManager.getCurrentSystemAvgLoad());
                        metricLog.put("inboundQpsThreshold",SystemRuleManager.getInboundQpsThreshold());
//                        RecordLog.warn(JSONUtil.createObj().set("metricLog",metricLog).toString());
                    }
                } catch (Exception e) {
                   // RecordLog.warn("[MetricTimerListener] Write metric error", e);
                }
            }
        }
    }

    private void aggregate(Map<Long, List<MetricNode>> maps, Map<Long, MetricNode> metrics, ClusterNode node) {
        for (Map.Entry<Long, MetricNode> entry : metrics.entrySet()) {
            long time = entry.getKey();
            MetricNode metricNode = entry.getValue();
            metricNode.setResource(node.getName());
            metricNode.setClassification(node.getResourceType());
            maps.computeIfAbsent(time, k -> new ArrayList<MetricNode>());
            List<MetricNode> nodes = maps.get(time);
            nodes.add(entry.getValue());
        }
    }

}