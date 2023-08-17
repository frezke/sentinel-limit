package com.aibank.framework.sentinellimit.domain;

import com.aibank.framework.sentinellimit.enums.LimitType;
import com.aibank.framework.sentinellimit.enums.SystemLimitType;

public class LimitData {
    /**
     * 限流类型
     */
    private LimitType limitType;

    /**
     * 系统限流类型
     */
    private SystemLimitType systemLimitType;

    /**
     * 系统负荷配置值
     */
    private double overloadConfigValue;

    /**
     * 系统负荷当前值
     */
    private double overloadValue;


    /**
     * 限流配置值
     */
    private double limitConfigValue;

    /**
     * 限流当前值
     */
    private double limitValue;

    public LimitData() {
    }

    public LimitData(LimitType limitType, SystemLimitType systemLimitType, double configValue, double value) {
        this.limitType = limitType;
        this.systemLimitType = systemLimitType;
        this.overloadConfigValue = configValue;
        this.overloadValue = value;
    }

    public LimitType getLimitType() {
        return limitType;
    }

    public void setLimitType(LimitType limitType) {
        this.limitType = limitType;
    }

    public SystemLimitType getSystemLimitType() {
        return systemLimitType;
    }

    public void setSystemLimitType(SystemLimitType systemLimitType) {
        this.systemLimitType = systemLimitType;
    }

    public double getOverloadConfigValue() {
        return overloadConfigValue;
    }

    public void setOverloadConfigValue(double overloadConfigValue) {
        this.overloadConfigValue = overloadConfigValue;
    }

    public double getOverloadValue() {
        return overloadValue;
    }

    public void setOverloadValue(double overloadValue) {
        this.overloadValue = overloadValue;
    }

    public double getLimitConfigValue() {
        return limitConfigValue;
    }

    public void setLimitConfigValue(double limitConfigValue) {
        this.limitConfigValue = limitConfigValue;
    }

    public double getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(double limitValue) {
        this.limitValue = limitValue;
    }
}
