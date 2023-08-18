package com.aibank.framework.sentinellimit.slot;

import com.aibank.framework.sentinellimit.rule.OverloadFlowRuleChecker;
import com.aibank.framework.sentinellimit.rule.OverloadFlowRuleManager;
import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowSlot;
import com.alibaba.csp.sentinel.spi.Spi;
import com.alibaba.csp.sentinel.util.function.Function;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Spi(order = -1500)
public class SystemLoadFlowSlot extends FlowSlot {
    private final OverloadFlowRuleChecker checker = new OverloadFlowRuleChecker();

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode node, int count,
                      boolean prioritized, Object... args) throws Throwable {
        checkFlow(resourceWrapper, context, node, count, prioritized);
        fireEntry(context, resourceWrapper, node, count, prioritized, args);
    }


    void checkFlow(ResourceWrapper resource, Context context, DefaultNode node, int count, boolean prioritized)
            throws BlockException {
        checker.checkFlow(ruleProvider, resource, context, node, count, prioritized);
    }

    private final Function<String, Collection<FlowRule>> ruleProvider = new Function<String, Collection<FlowRule>>() {
        @Override
        public Collection<FlowRule> apply(String resource) {
            Map<String, List<FlowRule>> flowRules = OverloadFlowRuleManager.getFlowRuleMap();
            return flowRules.get(resource);
        }
    };
}
