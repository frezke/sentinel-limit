package com.aibank.framework.sentinellimit.dao.entity;

import com.aibank.framework.sentinellimit.enums.LimitType;
import com.aibank.framework.sentinellimit.enums.SystemLimitType;

import java.util.Date;

/*create table `block_info` (
        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '阻塞信息id',
        `app` varchar(50) DEFAULT NULL COMMENT '系统编码',
        `resource` varchar(255) DEFAULT NULL COMMENT '资源名称',
        `totalRequest` bigint(20) DEFAULT NULL COMMENT '最近1分钟内的请求数',
        `totalPass` bigint(20) DEFAULT NULL COMMENT '最近1分钟内通过规则检测的请求数',
        `totalSuccess` bigint(20) DEFAULT NULL COMMENT '最近1分钟内通过规则检测完成调用并返回的请求数量',
        `totalQps` bigint(20) DEFAULT NULL COMMENT '每秒/此刻的请求数，包括（blockQps与passQps）',
        `passQps` bigint(20) DEFAULT NULL COMMENT '每秒/此刻通过规则检测的请求数量',
        `blockQps` bigint(20) DEFAULT NULL COMMENT '每秒/此刻该资源被拦截的数量',
        `successQps` bigint(20) DEFAULT NULL COMMENT '每秒/此刻通过规则检测完成调用并返回的请求数量',
        `curTheadNum` bigint(20) DEFAULT NULL COMMENT '此刻线程数量',
        `avgRt` bigint(20) DEFAULT NULL COMMENT '平均响应时间',
        `createTime` timestamp DEFAULT NULL COMMENT '每创建时间',
        PRIMARY KEY (`id`),
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Sentinel限流信息表'
*/

public class BlockInfoEntity {
    private Long id;    //阻塞信息id
    private String app;    //系统编码
    private LimitType limitType;    //限流类型，正常限流/系统限流
    private Double limitConfigValue;    //正常限流配置值
    private Double limitValue;    //当前限流值
    private SystemLimitType systemLimitType;    //系统限流类型，qps/thread/rt/load/cpu
    private Double overloadConfigValue;    //系统负荷配置值
    private String sentinelCause;    //限流原因
//正常限流监控指标
    private String resource;    //资源名称
    private Long totalRequest;    //最近1分钟内的请求数
    private Long totalPass;    //最近1分钟内通过规则检测的请求数
    private Long totalSuccess;    //最近1分钟内通过规则检测完成调用并返回的请求数量

    private Double totalQps;    //每秒/此刻的请求数，包括（blockQps与passQps）
    private Double passQps;    //每秒/此刻通过规则检测的请求数量
    private Double blockQps;    //每秒/此刻该资源被拦截的数量

//系统限流监控指标
    private Double qps;    //系统qps
    private Double thread;    //系统所占用线程数
    private Double rt;    //系统请求返回时间
    private Double load;    //系统负载
    private Double cpu;    //系统cpu使用率


//    private Date createTime;    //创建时间


//    private Double successQps;    //每秒/此刻通过规则检测完成调用并返回的请求数量
//    private Double exceptionQps;    //每秒/此刻异常的数量（不包括被拦截时所产生的异常）
//    private Double occupiedPassQps;    //每秒/此刻占用未来请求的数目

    public BlockInfoEntity() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setLimitType(LimitType limitType) {
        this.limitType = limitType;
    }

    public void setLimitConfigValue(Double limitConfigValue) {
        this.limitConfigValue = limitConfigValue;
    }

    public void setLimitValue(Double limitValue) {
        this.limitValue = limitValue;
    }

    public void setSystemLimitType(SystemLimitType systemLimitType) {
        this.systemLimitType = systemLimitType;
    }

    public void setOverloadConfigValue(Double overloadConfigValue) {
        this.overloadConfigValue = overloadConfigValue;
    }

    public void setSentinelCause(String sentinelCause) {
        this.sentinelCause = sentinelCause;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setTotalRequest(Long totalRequest) {
        this.totalRequest = totalRequest;
    }

    public void setTotalPass(Long totalPass) {
        this.totalPass = totalPass;
    }

    public void setTotalSuccess(Long totalSuccess) {
        this.totalSuccess = totalSuccess;
    }

    public void setTotalQps(Double totalQps) {
        this.totalQps = totalQps;
    }

    public void setPassQps(Double passQps) {
        this.passQps = passQps;
    }

    public void setBlockQps(Double blockQps) {
        this.blockQps = blockQps;
    }

    public void setQps(Double qps) {
        this.qps = qps;
    }

    public void setThread(Double thread) {
        this.thread = thread;
    }

    public void setRt(Double rt) {
        this.rt = rt;
    }

    public void setLoad(Double load) {
        this.load = load;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Long getId() {
        return id;
    }

    public String getApp() {
        return app;
    }

    public LimitType getLimitType() {
        return limitType;
    }

    public Double getLimitConfigValue() {
        return limitConfigValue;
    }

    public Double getLimitValue() {
        return limitValue;
    }

    public SystemLimitType getSystemLimitType() {
        return systemLimitType;
    }

    public Double getOverloadConfigValue() {
        return overloadConfigValue;
    }

    public String getSentinelCause() {
        return sentinelCause;
    }

    public String getResource() {
        return resource;
    }

    public Long getTotalRequest() {
        return totalRequest;
    }

    public Long getTotalPass() {
        return totalPass;
    }

    public Long getTotalSuccess() {
        return totalSuccess;
    }

    public Double getTotalQps() {
        return totalQps;
    }

    public Double getPassQps() {
        return passQps;
    }

    public Double getBlockQps() {
        return blockQps;
    }

    public Double getQps() {
        return qps;
    }

    public Double getThread() {
        return thread;
    }

    public Double getRt() {
        return rt;
    }

    public Double getLoad() {
        return load;
    }

    public Double getCpu() {
        return cpu;
    }

    @Override
    public String toString() {
        return "BlockInfoEntity{" +
                "id=" + id +
                ", app='" + app + '\'' +
                ", limitType=" + limitType +
                ", limitConfigValue=" + limitConfigValue +
                ", limitValue=" + limitValue +
                ", systemLimitType=" + systemLimitType +
                ", overloadConfigValue=" + overloadConfigValue +
                ", sentinelCause='" + sentinelCause + '\'' +
                ", resource='" + resource + '\'' +
                ", totalRequest=" + totalRequest +
                ", totalPass=" + totalPass +
                ", totalSuccess=" + totalSuccess +
                ", totalQps=" + totalQps +
                ", passQps=" + passQps +
                ", blockQps=" + blockQps +
                ", qps=" + qps +
                ", thread=" + thread +
                ", rt=" + rt +
                ", load=" + load +
                ", cpu=" + cpu +
                '}';
    }
}
