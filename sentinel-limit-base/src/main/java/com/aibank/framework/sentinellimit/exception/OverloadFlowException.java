package com.aibank.framework.sentinellimit.exception;

import com.aibank.framework.sentinellimit.domain.LimitData;
import com.alibaba.csp.sentinel.slots.block.AbstractRule;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

/***
 * @author youji.zj
 */
public class OverloadFlowException extends BlockException {

    private LimitData limitData;

    public OverloadFlowException(String ruleLimitApp) {
        super(ruleLimitApp);
    }

    public OverloadFlowException(String ruleLimitApp, FlowRule rule) {
        super(ruleLimitApp, rule);
    }

    public OverloadFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverloadFlowException(String ruleLimitApp, String message) {
        super(ruleLimitApp, message);
    }

    public OverloadFlowException(String ruleLimitApp, AbstractRule rule, LimitData limitData) {
        super(ruleLimitApp, rule);
        this.limitData = limitData;
    }

    public LimitData getLimitData() {
        return limitData;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    /**
     * Get triggered rule.
     * Note: the rule result is a reference to rule map and SHOULD NOT be modified.
     *
     * @return triggered rule
     * @since 1.4.2
     */
    @Override
    public FlowRule getRule() {
        return rule.as(FlowRule.class);
    }
}
