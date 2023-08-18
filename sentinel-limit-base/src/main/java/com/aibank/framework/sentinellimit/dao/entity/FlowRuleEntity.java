package com.aibank.framework.sentinellimit.dao.entity;


public class FlowRuleEntity {
    private Long id;
    private String app;
    private String resource;
    private Integer controlBehavior;
    private Boolean effectOnOverLoad;
    private Double count;
    private Integer grade;
    private String limitApp;
    private Integer strategy;
    private int maxQueueingTimeMs;
    private Boolean open;
    private String createTime;
    private String updateTime;
    private String createdBy;
    private String updatedBy;


    public FlowRuleEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getControlBehavior() {
        return controlBehavior;
    }

    public void setControlBehavior(Integer controlBehavior) {
        this.controlBehavior = controlBehavior;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getLimitApp() {
        return limitApp;
    }

    public void setLimitApp(String limitApp) {
        this.limitApp = limitApp;
    }

    public Integer getStrategy() {
        return strategy;
    }

    public void setStrategy(Integer strategy) {
        this.strategy = strategy;
    }

    public Boolean getEffectOnOverLoad() {
        return effectOnOverLoad;
    }

    public void setEffectOnOverLoad(Boolean effectOnOverLoad) {
        this.effectOnOverLoad = effectOnOverLoad;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
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

    public int getMaxQueueingTimeMs() {
        return maxQueueingTimeMs;
    }

    public void setMaxQueueingTimeMs(int maxQueueingTimeMs) {
        this.maxQueueingTimeMs = maxQueueingTimeMs;
    }
}