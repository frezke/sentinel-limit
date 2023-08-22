package com.aibank.framework.sentinellimit.flow;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.spi.Spi;

import java.util.ArrayList;
import java.util.List;

@Spi(order = -1500)
public class RuntimeFlowRuleSupplier implements FlowRuleSupplier {


    @Override
    public List<FlowRule> getFlowRule() {
        return new ArrayList<>();
    }

}
