package com.aibank.framework.sentinellimit.task;

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
                        StringBuilder metricLog = new StringBuilder(32);
                        metricLog.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(metricNode.getTimestamp()))).append("|");
                        metricLog.append(metricNode.getResource()).append("|");
                        metricLog.append(metricNode.getPassQps()).append("|");
                        metricLog.append(metricNode.getBlockQps()).append("|");
                        metricLog.append(metricNode.getSuccessQps()).append("|");
                        metricLog.append(metricNode.getRt()).append("|");
                        metricLog.append(Constants.ENTRY_NODE.passQps()).append("|");
                        metricLog.append(Constants.ENTRY_NODE.curThreadNum()).append("|");
                        metricLog.append(Constants.ENTRY_NODE.avgRt()).append("|");
                        metricLog.append(SystemRuleManager.getCurrentSystemAvgLoad()).append("|");
                        metricLog.append(SystemRuleManager.getCurrentCpuUsage()).append("|");
                        metricLog.append(metricNode.getExceptionQps()).append("|");
                        metricLog.append(metricNode.getOccupiedPassQps()).append("|");
                        metricLog.append(metricNode.getConcurrency()).append("|");
                        metricLog.append(metricNode.getClassification()).append("|");
                        RecordLog.warn(metricLog.toString());
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