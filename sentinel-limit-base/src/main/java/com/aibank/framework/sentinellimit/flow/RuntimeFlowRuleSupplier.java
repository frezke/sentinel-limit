package com.aibank.framework.sentinellimit.flow;

import com.aibank.framework.sentinellimit.service.RateLimitUtil;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RuntimeFlowRuleSupplier implements FlowRuleSupplier {

    private static Map<String, FlowRule> flowRules = new ConcurrentHashMap<>();

    public static void addFlowRule(FlowRule flowRule) {
        if (flowRule == null || flowRules.containsKey(RateLimitUtil.getRuleId(flowRule))) {
            return;
        }
        flowRules.put(RateLimitUtil.getRuleId(flowRule), flowRule);

        List<FlowRule> loadedRules = FlowRuleManager.getRules();
        Optional<FlowRule> ruleOptional = loadedRules.stream().filter(rule -> RateLimitUtil.equals(rule, flowRule)).findAny();
        if (ruleOptional.isPresent()) {
            return;
        }
        loadedRules.add(flowRule);
        FlowRuleManager.loadRules(new ArrayList<>(loadedRules));
    }


    @Override
    public List<FlowRule> getFlowRule() {
        return new ArrayList<>(flowRules.values());
    }

    @Override
    public int getOrder() {
        return 1000;
    }

}
