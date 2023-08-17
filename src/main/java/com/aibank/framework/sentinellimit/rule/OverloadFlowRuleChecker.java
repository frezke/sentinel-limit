package com.aibank.framework.sentinellimit.rule;

import com.aibank.framework.sentinellimit.domain.LimitData;
import com.aibank.framework.sentinellimit.enums.LimitType;
import com.aibank.framework.sentinellimit.enums.SystemLimitType;
import com.aibank.framework.sentinellimit.exception.OverloadFlowException;
import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleChecker;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.util.function.Function;

import java.util.Collection;

public class OverloadFlowRuleChecker extends FlowRuleChecker {

    public void checkFlow(Function<String, Collection<FlowRule>> ruleProvider, ResourceWrapper resource, Context context, DefaultNode node, int count, boolean prioritized) throws BlockException {
        if (ruleProvider == null || resource == null) {
            return;
        }
        Collection<FlowRule> rules = ruleProvider.apply(resource.getName());
        if (rules != null) {
            for (FlowRule rule : rules) {
                LimitData limitData = canPassCheck(resource, rule, context, node, count, prioritized);
                if (limitData != null) {
                    throw new OverloadFlowException(rule.getLimitApp(), rule, limitData);
                }
            }
        }
    }

    public LimitData canPassCheck(ResourceWrapper resource, FlowRule rule, Context context, DefaultNode node, int acquireCount, boolean prioritized) {
        LimitData limitData = triggerSystemOverload(resource, acquireCount);
        if (limitData != null) {
            boolean canPass = super.canPassCheck(rule, context, node, acquireCount, prioritized);
            if (!canPass) {
                return limitData;
            }
        }
        return null;
    }

    public static LimitData triggerSystemOverload(ResourceWrapper resourceWrapper, int count) {
        if (resourceWrapper == null) {
            return null;
        }
        // Ensure the checking switch is on.
        if (!GlobalOverloadConfig.isCheckSystemStatus()) {
            return null;
        }

        // for inbound traffic only
        if (resourceWrapper.getEntryType() != EntryType.IN) {
            return null;
        }
        // total qps
        double currentQps = Constants.ENTRY_NODE.passQps();
        if (currentQps + count > GlobalOverloadConfig.getMaxQps()) {
            return new LimitData(LimitType.overloadFlowRule, SystemLimitType.qps, GlobalOverloadConfig.getMaxQps(), currentQps + count);
        }

        // total thread
        int currentThread = Constants.ENTRY_NODE.curThreadNum();
        if (currentThread > GlobalOverloadConfig.getMaxThread()) {
            return new LimitData(LimitType.overloadFlowRule, SystemLimitType.thread, GlobalOverloadConfig.getMaxThread(), currentThread);
        }

        double rt = Constants.ENTRY_NODE.avgRt();
        if (rt > GlobalOverloadConfig.getMaxRt()) {
            return new LimitData(LimitType.overloadFlowRule, SystemLimitType.rt, GlobalOverloadConfig.getMaxRt(), rt);
        }

        // load. BBR algorithm.
        double currentSystemAvgLoad = SystemRuleManager.getCurrentSystemAvgLoad();
        if (currentSystemAvgLoad > GlobalOverloadConfig.getMaxSystemLoad()) {
            if (!checkBbr(currentThread)) {
                return new LimitData(LimitType.overloadFlowRule, SystemLimitType.load, GlobalOverloadConfig.getMaxSystemLoad(), currentSystemAvgLoad);
            }
        }

        // cpu usage
        double currentCpuUsage = SystemRuleManager.getCurrentCpuUsage();
        if (currentCpuUsage > GlobalOverloadConfig.getMaxCpuUsage()) {
            return new LimitData(LimitType.overloadFlowRule, SystemLimitType.cpu, GlobalOverloadConfig.getMaxCpuUsage(), currentCpuUsage);
        }
        return null;
    }

    private static boolean checkBbr(int currentThread) {
        return currentThread <= 1 || !((double) currentThread > Constants.ENTRY_NODE.maxSuccessQps() * Constants.ENTRY_NODE.minRt() / 1000.0);
    }

}
