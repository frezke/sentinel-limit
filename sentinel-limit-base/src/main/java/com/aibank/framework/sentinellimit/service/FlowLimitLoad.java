package com.aibank.framework.sentinellimit.service;

public interface FlowLimitLoad {

    void flowRule();

    void overloadFlowRule();

    void systemRule();
}
