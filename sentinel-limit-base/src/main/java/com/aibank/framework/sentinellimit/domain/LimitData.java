package com.aibank.framework.sentinellimit.domain;

import com.aibank.framework.sentinellimit.enums.LimitType;
import com.aibank.framework.sentinellimit.enums.SystemLimitType;
import com.alibaba.csp.sentinel.EntryType;

public class LimitData {

    private Long timestamp;

    /**
     * 资源名称
     */
    private String resource;


    private EntryType entryType;
    /**
     * 限流类型
     */
    private LimitType limitType;

    /**
     * 系统限流类型
     */
    private SystemLimitType systemLimitType;
    /**
     * 请求 id
     */
    private String transId;
    /**
     * 系统负荷配置值
     */
    private Double overloadConfigValue;

    /**
     * 系统负荷当前值
     */
    private Double overloadValue;


    /**
     * 限流配置值
     */
    private Double limitConfigValue;

    /**
     * 限流当前值
     */
    private Double limitValue;

    /**
     * 每秒请求数，包括（blockQps与passQps）
     */
    private Double totalQps;

    /**
     * 每秒通过的请求数量
     */
    private Double passQps;

    /**
     * 每秒被拦截的请求数量
     */
    private Double blockQps;


    /**
     * 最近1分钟内的请求数
     */
    private Long totalRequest;

    /**
     * 最近1分钟内通过的请求数量
     */
    private Long totalPass;

    /**
     * 最近1分钟内被拦截的请求数量
     */
    private Long totalBlock;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public LimitData() {
    }

    public LimitData(String resource, LimitType limitType, SystemLimitType systemLimitType, EntryType entryType, double configValue, double value) {
        this.entryType = entryType;
        this.limitType = limitType;
        this.resource = resource;
        this.systemLimitType = systemLimitType;
        this.overloadConfigValue = configValue;
        this.overloadValue = value;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
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

    public Double getOverloadConfigValue() {
        return overloadConfigValue;
    }

    public void setOverloadConfigValue(Double overloadConfigValue) {
        this.overloadConfigValue = overloadConfigValue;
    }

    public Double getOverloadValue() {
        return overloadValue;
    }

    public void setOverloadValue(Double overloadValue) {
        this.overloadValue = overloadValue;
    }

    public Double getLimitConfigValue() {
        return limitConfigValue;
    }

    public void setLimitConfigValue(Double limitConfigValue) {
        this.limitConfigValue = limitConfigValue;
    }

    public Double getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(Double limitValue) {
        this.limitValue = limitValue;
    }

    public Double getTotalQps() {
        return totalQps;
    }

    public void setTotalQps(Double totalQps) {
        this.totalQps = totalQps;
    }

    public Double getPassQps() {
        return passQps;
    }

    public void setPassQps(Double passQps) {
        this.passQps = passQps;
    }

    public Double getBlockQps() {
        return blockQps;
    }

    public void setBlockQps(Double blockQps) {
        this.blockQps = blockQps;
    }

    public Long getTotalRequest() {
        return totalRequest;
    }

    public void setTotalRequest(Long totalRequest) {
        this.totalRequest = totalRequest;
    }

    public Long getTotalPass() {
        return totalPass;
    }

    public void setTotalPass(Long totalPass) {
        this.totalPass = totalPass;
    }

    public Long getTotalBlock() {
        return totalBlock;
    }

    public void setTotalBlock(Long totalBlock) {
        this.totalBlock = totalBlock;
    }

    @Override
    public String toString() {
        return "LimitData{" +
                "timestamp=" + timestamp +
                ", resource='" + resource + '\'' +
                ", entryType=" + entryType +
                ", limitType=" + limitType +
                ", systemLimitType=" + systemLimitType +
                ", transId='" + transId + '\'' +
                ", overloadConfigValue=" + overloadConfigValue +
                ", overloadValue=" + overloadValue +
                ", limitConfigValue=" + limitConfigValue +
                ", limitValue=" + limitValue +
                ", totalQps=" + totalQps +
                ", passQps=" + passQps +
                ", blockQps=" + blockQps +
                ", totalRequest=" + totalRequest +
                ", totalPass=" + totalPass +
                ", totalBlock=" + totalBlock +
                '}';
    }
}
