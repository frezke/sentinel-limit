package com.aibank.framework.sentinellimit.flow;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

import java.util.ArrayList;
import java.util.List;

public class AnnotationFlowRuleSupplier implements FlowRuleSupplier {

    @Override
    public List<FlowRule> getFlowRule() {
        //TODO 扫描带有RateLimit 的方法,解析字段,复用自带的注解拦截器
        return new ArrayList<>();
    }

    @Override
    public int getOrder() {
        return 1000;
    }

}
