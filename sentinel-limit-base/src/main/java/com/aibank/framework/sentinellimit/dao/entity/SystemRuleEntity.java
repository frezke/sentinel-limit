package com.aibank.framework.sentinellimit.dao.entity;



// ddl

public class SystemRuleEntity {
    private Long id;
    private String app;
    private Double highestSystemLoad;
    private Double highestCpuUsage;
    private Double qps;
    private Long avgRt;
    private Long maxThread;
    private Boolean systemOverloadFlag;
    private Boolean open;
    private String createTime;
    private String updateTime;
    private String createdBy;
    private String updatedBy;


    public SystemRuleEntity() {
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

    public Double getHighestSystemLoad() {
        return highestSystemLoad;
    }

    public void setHighestSystemLoad(Double highestSystemLoad) {
        this.highestSystemLoad = highestSystemLoad;
    }

    public Double getHighestCpuUsage() {
        return highestCpuUsage;
    }

    public void setHighestCpuUsage(Double highestCpuUsage) {
        this.highestCpuUsage = highestCpuUsage;
    }

    public Double getQps() {
        return qps;
    }

    public void setQps(Double qps) {
        this.qps = qps;
    }

    public Long getAvgRt() {
        return avgRt;
    }

    public void setAvgRt(Long avgRt) {
        this.avgRt = avgRt;
    }

    public Long getMaxThread() {
        return maxThread;
    }

    public void setMaxThread(Long maxThread) {
        this.maxThread = maxThread;
    }

    public Boolean getSystemOverloadFlag() {
        return systemOverloadFlag;
    }

    public void setSystemOverloadFlag(Boolean systemOverloadFlag) {
        this.systemOverloadFlag = systemOverloadFlag;
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
}
