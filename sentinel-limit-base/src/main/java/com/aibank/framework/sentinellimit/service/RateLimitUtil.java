
package com.aibank.framework.sentinellimit.service;


import com.aibank.framework.sentinellimit.domain.LimitConstants;
import com.aibank.framework.sentinellimit.flow.RuntimeFlowRuleSupplier;
import com.alibaba.csp.sentinel.*;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

public class RateLimitUtil {

    private RateLimitUtil() {
    }

    public static boolean entry(String name) {
        return entry(name, RuleConstant.FLOW_GRADE_QPS, EntryType.OUT, null);
    }

    public static boolean entry(String name, Double limitCount) {
        return entry(name, RuleConstant.FLOW_GRADE_QPS, EntryType.OUT, limitCount);
    }

    public static boolean entry(String name, EntryType trafficType, Double limitCount) {
        return entry(name, RuleConstant.FLOW_GRADE_QPS, trafficType, limitCount);
    }

    public static boolean entry(String name, EntryType trafficType) {
        return entry(name, RuleConstant.FLOW_GRADE_QPS, trafficType, null);
    }


    public static boolean wait(String name) {
        return wait(name, RuleConstant.FLOW_GRADE_THREAD, EntryType.OUT, null, 30000);
    }


    public static boolean wait(String name,Double limitCount) {
        return wait(name, RuleConstant.FLOW_GRADE_THREAD, EntryType.OUT, limitCount, 30000);
    }

    public static boolean wait(String name, int maxQueueingTimeMs) {
        return wait(name, RuleConstant.FLOW_GRADE_THREAD, EntryType.OUT, null, maxQueueingTimeMs);
    }

    public static boolean wait(String name, EntryType trafficType, int maxQueueingTimeMs) {
        return wait(name, RuleConstant.FLOW_GRADE_THREAD, trafficType, null, maxQueueingTimeMs);
    }

    public static boolean wait(String name, EntryType trafficType, Double limitCount) {
        return wait(name, RuleConstant.FLOW_GRADE_THREAD, trafficType, limitCount, 30000);
    }

    public static boolean wait(String name, Double limitCount, int maxQueueingTimeMs) {
        return wait(name, RuleConstant.FLOW_GRADE_THREAD, EntryType.OUT, limitCount, maxQueueingTimeMs);
    }

    public static boolean wait(String name, EntryType trafficType, Double limitCount, int maxQueueingTimeMs) {
        return wait(name, RuleConstant.FLOW_GRADE_THREAD, trafficType, limitCount, maxQueueingTimeMs);
    }


    public static boolean entry(String name, int grade, EntryType trafficType, Double limitCount) {
        return entry(LimitConstants.CONTEXT_DEFAULT_NAME, RuleConstant.LIMIT_APP_DEFAULT, name, grade, trafficType, limitCount, 1, RuleConstant.CONTROL_BEHAVIOR_DEFAULT, 0);
    }

    public static boolean wait(String name, int grade, EntryType trafficType, Double limitCount, int maxQueueingTimeMs) {
        return entry(LimitConstants.CONTEXT_DEFAULT_NAME, RuleConstant.LIMIT_APP_DEFAULT, name, grade, trafficType, limitCount, 1, RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER, maxQueueingTimeMs);
    }


    /**
     * 无等待
     *
     * @param context
     * @param origin
     * @param name
     * @param grade
     * @param trafficType
     * @param limitCount
     * @return
     */
    public static boolean entry(String context, String origin, String name, int grade, EntryType trafficType, Double limitCount, int batchCount, int controlBehavior, int maxQueueingTimeMs) {
        if (limitCount != null && limitCount > 0) {
            FlowRule flowRule = new FlowRule();
            flowRule.setResource(name);
            flowRule.setCount(limitCount);
            flowRule.setGrade(grade);
            flowRule.setLimitApp(origin);
            flowRule.setStrategy(0);
            flowRule.setControlBehavior(controlBehavior);
            flowRule.setClusterMode(false);
            flowRule.setMaxQueueingTimeMs(maxQueueingTimeMs);
            RuntimeFlowRuleSupplier.addFlowRule(flowRule);
        }

        Entry entry = null;
        try {
            ContextUtil.enter(context, origin);
            entry = SphU.entry(name, trafficType, batchCount);
            if (entry != null) {
                return true;
            } else {
                return false;
            }
        } catch (BlockException e) {
            return false;
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    public static String getRuleId(FlowRule flowRule) {
        return flowRule.getResource() + "_" + flowRule.getGrade() + "_" + flowRule.getLimitApp();
    }

    public static boolean equals(FlowRule flowRule1, FlowRule flowRule2) {
        return getRuleId(flowRule1).equals(getRuleId(flowRule2));
    }
}
