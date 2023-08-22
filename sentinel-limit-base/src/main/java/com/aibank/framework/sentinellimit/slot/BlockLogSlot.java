package com.aibank.framework.sentinellimit.slot;

import com.aibank.framework.sentinellimit.domain.LimitData;
import com.aibank.framework.sentinellimit.domain.TransIdHolder;
import com.aibank.framework.sentinellimit.enums.LimitType;
import com.aibank.framework.sentinellimit.enums.SystemLimitType;
import com.aibank.framework.sentinellimit.exception.OverloadFlowException;
import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.AbstractRule;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.spi.Spi;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Spi(order = Constants.ORDER_LOG_SLOT)
public class BlockLogSlot extends AbstractLinkedProcessorSlot<DefaultNode> {
    public static final BlockingQueue<LimitData> BLOCK_LOG_QUEUE = new ArrayBlockingQueue(10000);

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode obj, int count, boolean prioritized, Object... args)
            throws Throwable {
        try {
            fireEntry(context, resourceWrapper, obj, count, prioritized, args);
        } catch (BlockException e) {
            LimitData limitData = getLimitData(e, resourceWrapper, obj, count);
            limitData.setTransId(TransIdHolder.getTransId());
            //打印日志
            // RecordLog.warn("trigger limited : {}", limitData);

            boolean offered = BLOCK_LOG_QUEUE.offer(limitData);
            if (!offered) {
                //     RecordLog.warn("put block  error ,queue is full ");
            }
            throw e;
        } catch (Throwable e) {
            RecordLog.warn("Unexpected entry exception", e);
        }

    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        try {
            fireExit(context, resourceWrapper, count, args);
        } catch (Throwable e) {
            RecordLog.warn("Unexpected entry exit exception", e);
        }
    }


    private LimitData getLimitData(BlockException e, ResourceWrapper resourceWrapper, DefaultNode node, int count) {

        LimitData limitData = new LimitData();
        if (e instanceof FlowException) {
            FlowRule rule = (FlowRule) e.getRule();
            limitData.setLimitType(LimitType.flowRule);
            double limitValue = rule.getGrade() == RuleConstant.FLOW_GRADE_THREAD ? (double) node.curThreadNum() : (int) (node.passQps());
            limitData.setLimitValue(limitValue);
            limitData.setLimitConfigValue(rule.getCount());
        } else if (e instanceof SystemBlockException) {
            SystemBlockException systemBlockException = (SystemBlockException) e;
            SystemLimitType systemLimitType = SystemLimitType.valueOf(systemBlockException.getLimitType());

            //非限流那一刻的值,可能有误差
            double overloadConfigValue = 0;
            double overloadValue = 0;
            switch (systemLimitType) {
                case rt:
                    overloadConfigValue = SystemRuleManager.getRtThreshold();
                    overloadValue = Constants.ENTRY_NODE.avgRt();
                    break;
                case qps:
                    overloadConfigValue = SystemRuleManager.getInboundQpsThreshold();
                    overloadValue = Constants.ENTRY_NODE.passQps() + count;
                    break;
                case cpu:
                    overloadConfigValue = SystemRuleManager.getCpuUsageThreshold();
                    overloadValue = SystemRuleManager.getCurrentCpuUsage();
                    break;
                case load:
                    overloadConfigValue = SystemRuleManager.getSystemLoadThreshold();
                    overloadValue = SystemRuleManager.getCurrentSystemAvgLoad();
                    break;
                case thread:
                    overloadConfigValue = SystemRuleManager.getMaxThreadThreshold();
                    overloadValue = Constants.ENTRY_NODE.curThreadNum();
                    break;

            }
            limitData.setLimitType(LimitType.systemRule);
            limitData.setSystemLimitType(systemLimitType);
            limitData.setOverloadConfigValue(overloadConfigValue);
            limitData.setOverloadValue(overloadValue);
        } else if (e instanceof OverloadFlowException) {
            FlowRule rule = (FlowRule) e.getRule();
            limitData = ((OverloadFlowException) e).getLimitData();
            double limitValue = rule.getGrade() == RuleConstant.FLOW_GRADE_THREAD ? (double) node.curThreadNum() : (int) (node.passQps());
            limitData.setLimitValue(limitValue);
            limitData.setLimitConfigValue(rule.getCount());
        }
        limitData.setResource(resourceWrapper.getName());
        limitData.setEntryType(resourceWrapper.getEntryType());
        limitData.setTimestamp(System.currentTimeMillis());

        limitData.setTotalPass(node.totalPass());
        limitData.setTotalRequest(node.totalRequest());
        limitData.setTotalBlock(node.blockRequest());

        limitData.setTotalQps(node.totalQps());
        limitData.setPassQps(node.passQps());
        limitData.setBlockQps(node.blockQps());
        return limitData;
    }

}
