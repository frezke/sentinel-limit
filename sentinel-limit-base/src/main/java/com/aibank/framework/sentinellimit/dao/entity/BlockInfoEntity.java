package com.aibank.framework.sentinellimit.dao.entity;

import java.util.Date;


// create table block_info ddl
/*
          CREATE TABLE `block_info` (
            `id` bigint(20) NOT NULL AUTO_INCREMENT,
            `timestamp` bigint(20) DEFAULT NULL COMMENT '时间戳',
            `time` datetime DEFAULT NULL COMMENT '时间',
            `trans_id` varchar(255) DEFAULT NULL COMMENT '请求 id',
            `app` varchar(255) DEFAULT NULL COMMENT '系统编码',
            `resource` varchar(255) DEFAULT NULL COMMENT '资源名称',
            `entry_type` varchar(255) DEFAULT NULL COMMENT '入口类型',
            `limit_type` varchar(255) DEFAULT NULL COMMENT '限流类型',
            `system_limit_type` varchar(255) DEFAULT NULL COMMENT '系统限流类型',
            `overload_config_value` double DEFAULT NULL COMMENT '系统负荷配置值',
            `overload_value` double DEFAULT NULL COMMENT '系统负荷当前值',
            `limit_config_value` double DEFAULT NULL COMMENT '限流配置值',
            `limit_value` double DEFAULT NULL COMMENT '限流当前值',
            `total_qps` double DEFAULT NULL COMMENT '每秒请求数，包括（blockQps与passQps）',
            `pass_qps` double DEFAULT NULL COMMENT '每秒通过的请求数量',
            `block_qps` double DEFAULT NULL COMMENT '每秒被拦截的请求数量',
            `total_request` bigint(20) DEFAULT NULL COMMENT '最近1分钟内的请求数',
            `total_pass` bigint(20) DEFAULT NULL COMMENT '最近1分钟内通过的请求数量',
            `total_block` bigint(20) DEFAULT NULL COMMENT '最近1分钟内被拦截的请求数量',
            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
            `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
            `updated_by` varchar(255) DEFAULT NULL COMMENT '更新人',
            PRIMARY KEY (`id`)
            ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='限流信息表';
    */


public class BlockInfoEntity {

    /**
     * id
     */
    private Long id;


    private Long timestamp;

    private Date time;

    /**
     * 请求 id
     */
    private String transId;

    /**
     * 系统编码
     */
    private String app;

    /**
     * 资源名称
     */
    private String resource;

    private String entryType;
    /**
     * 限流类型
     */
    private String limitType;

    /**
     * 系统限流类型
     */
    private String systemLimitType;

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


    private String createTime;
    private String updateTime;
    private String createdBy;
    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public String getSystemLimitType() {
        return systemLimitType;
    }

    public void setSystemLimitType(String systemLimitType) {
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
