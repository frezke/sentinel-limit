package com.aibank.framework.sentinellimit.service;

import com.aibank.framework.sentinellimit.flow.FlowRuleSupplier;

import java.util.List;

public interface FlowLimitLoad {

    List<FlowRuleSupplier> getFlowRuleSuppliers();

    void flowRule();

    void overloadFlowRule();

    void systemRule();
}
