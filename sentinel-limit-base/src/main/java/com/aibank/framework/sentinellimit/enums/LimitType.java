package com.aibank.framework.sentinellimit.enums;

public enum LimitType {

    // 正常限流
    flowRule,
    // 系统高负荷限流
    overloadFlowRule,
    //系统负载限流
    systemRule,
    Null;
}
