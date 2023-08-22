package com.aibank.framework.sentinellimit.flow;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

import java.util.List;

public interface FlowRuleSupplier {
    List<FlowRule> getFlowRule();
}
