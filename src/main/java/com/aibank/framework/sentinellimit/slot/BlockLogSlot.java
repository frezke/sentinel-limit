package com.aibank.framework.sentinellimit.slot;

import com.aibank.framework.sentinellimit.dao.entity.BlockInfoEntity;
import com.aibank.framework.sentinellimit.domain.LimitData;
import com.aibank.framework.sentinellimit.enums.LimitType;
import com.aibank.framework.sentinellimit.enums.SystemLimitType;
import com.aibank.framework.sentinellimit.exception.OverloadFlowException;
import com.aibank.framework.sentinellimit.rule.GlobalOverloadConfig;
import com.aibank.framework.sentinellimit.service.DefaultBlockRequestInfoRecord;
import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.spi.Spi;
import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.util.Date;

@Spi(order = Constants.ORDER_LOG_SLOT)
public class BlockLogSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    public static DataSource createDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sentinel?characterEncoding=utf8");
        dataSource.setUsername("root");
        dataSource.setPassword("rootroot");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(5);
        dataSource.setMaxActive(20);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        return dataSource;
    }

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode obj, int count, boolean prioritized, Object... args)
            throws Throwable {
        try {
            fireEntry(context, resourceWrapper, obj, count, prioritized, args);
        } catch (BlockException e) {
            LimitData limitData = getLimitData(e, obj, count);
            // TODO  打印日志,入库
//            StringBuffer stringBuffer = new StringBuffer();
//            stringBuffer
//                    .append("totalRequest: ").append(obj.totalRequest()).append("\n")
//                    .append("totalPass: ").append(obj.totalPass()).append("\n")
//                    .append("totalSuccess: ").append(obj.totalSuccess()).append("\n")
//                    .append("totalException: ").append(obj.totalException()).append("\n")
//                    .append("blockQps: ").append(obj.blockQps()).append("\n")
//                    .append("passQps: ").append(obj.passQps()).append("\n")
//                    .append("successQps: ").append(obj.successQps()).append("\n")
//                    .append("exceptionQps: ").append(obj.exceptionQps()).append("\n")
//                    .append("blockQps: ").append(obj.blockQps()).append("\n")
//                    .append("rt: ").append(obj.avgRt()).append("\n")
//                    .append("curTheadNum: ").append(obj.curThreadNum()).append("\n")
//                    .append("metric: ").append(obj.metrics()).append("\n")
//                    .append("totalQps: ").append(obj.totalQps()).append("\n")
//                    .append("occupiedPassQps: ").append(obj.occupiedPassQps());
            if (limitData.getLimitType().equals(LimitType.flowRule)) {
                DataSource dataSource = createDataSource();
                DefaultBlockRequestInfoRecord defaultBlockRequestInfoRecord = new DefaultBlockRequestInfoRecord(dataSource, "237000");
                BlockInfoEntity blockInfoEntity = new BlockInfoEntity();
                blockInfoEntity.setId(System.currentTimeMillis());
                blockInfoEntity.setLimitType(limitData.getLimitType());
                blockInfoEntity.setLimitConfigValue(limitData.getLimitConfigValue());
                blockInfoEntity.setLimitValue(limitData.getLimitValue());
                blockInfoEntity.setSentinelCause("通过正常限流规则检测规则不通过从而限流！当前限流值：" + limitData.getLimitConfigValue());
                blockInfoEntity.setResource(resourceWrapper.getName());
                blockInfoEntity.setTotalRequest(obj.totalRequest());
                blockInfoEntity.setTotalPass(obj.totalPass());
                blockInfoEntity.setTotalSuccess(obj.totalSuccess());
                blockInfoEntity.setTotalQps(obj.totalQps());
                blockInfoEntity.setPassQps(obj.passQps());
                blockInfoEntity.setBlockQps(obj.blockQps());

                defaultBlockRequestInfoRecord.blockInfoRecord(blockInfoEntity);

            } else {
                DataSource dataSource = createDataSource();
                DefaultBlockRequestInfoRecord defaultBlockRequestInfoRecord = new DefaultBlockRequestInfoRecord(dataSource, "237000");
                BlockInfoEntity blockInfoEntity = new BlockInfoEntity();
                blockInfoEntity.setId(System.currentTimeMillis());
                blockInfoEntity.setLimitType(limitData.getLimitType());
                blockInfoEntity.setSystemLimitType(limitData.getSystemLimitType());
                blockInfoEntity.setOverloadConfigValue(limitData.getOverloadConfigValue());
                SystemLimitType systemLimitType = limitData.getSystemLimitType();
                switch (systemLimitType) {
                    case rt:
                        blockInfoEntity.setSentinelCause("请求返回时间限流！当前限流值：" + limitData.getLimitConfigValue());
                        blockInfoEntity.setRt(limitData.getOverloadValue());
                        break;
                    case qps:
                        blockInfoEntity.setSentinelCause("系统qps限流！当前限流值：" + limitData.getLimitConfigValue());
                        blockInfoEntity.setQps(limitData.getOverloadValue());
                        break;
                    case cpu:
                        blockInfoEntity.setSentinelCause("系统cpu使用率限流！当前限流值：" + limitData.getLimitConfigValue());
                        blockInfoEntity.setCpu(limitData.getOverloadValue());
                        break;
                    case load:
                        blockInfoEntity.setSentinelCause("系统负载限流！当前限流值：" + limitData.getLimitConfigValue());
                        blockInfoEntity.setLoad(limitData.getOverloadValue());
                        break;
                    case thread:
                        blockInfoEntity.setSentinelCause("系统线程！当前限流值：" + limitData.getLimitConfigValue());
                        blockInfoEntity.setThread(limitData.getOverloadValue());
                        break;
                }
                defaultBlockRequestInfoRecord.blockInfoRecord(blockInfoEntity);
            }

//            OverloadFlowException  overloadFlowException = (OverloadFlowException)e;
//            System.out.println(overloadFlowException.);

           // System.out.println(stringBuffer);
            throw e;
        } catch (Throwable e) {
            System.out.println(e.getStackTrace());
            RecordLog.warn("Unexpected entry exception", e);
        }
    }

    private LimitData getLimitData(BlockException e, DefaultNode node, int count) {
        LimitData limitData = new LimitData();
        if (e instanceof FlowException) {
            FlowRule rule = (FlowRule) e.getRule();
            limitData.setLimitType(LimitType.flowRule);
            //只考虑 qps,不考虑线程数
            limitData.setLimitValue(node.passQps());
            limitData.setLimitConfigValue(rule.getCount());
        } else if (e instanceof SystemBlockException) {
            SystemBlockException systemBlockException = (SystemBlockException) e;
            SystemLimitType systemLimitType = SystemLimitType.valueOf(systemBlockException.getLimitType());

            //非限流那一刻的值,可能有误差
            double overloadConfigValue = 0;
            double overloadValue = 0;
            switch (systemLimitType) {
                case rt:
                    overloadConfigValue = SystemRuleManager.getRtThreshold();
                    overloadValue = Constants.ENTRY_NODE.avgRt();
                    break;
                case qps:
                    overloadConfigValue = SystemRuleManager.getInboundQpsThreshold();
                    overloadValue = Constants.ENTRY_NODE.passQps() + count;
                    break;
                case cpu:
                    overloadConfigValue = SystemRuleManager.getCpuUsageThreshold();
                    overloadValue = SystemRuleManager.getCurrentCpuUsage();
                    break;
                case load:
                    overloadConfigValue = SystemRuleManager.getSystemLoadThreshold();
                    overloadValue = SystemRuleManager.getCurrentSystemAvgLoad();
                    break;
                case thread:
                    overloadConfigValue = SystemRuleManager.getMaxThreadThreshold();
                    overloadValue = Constants.ENTRY_NODE.curThreadNum();
                    break;

            }
            limitData.setLimitType(LimitType.systemRule);
            limitData.setSystemLimitType(systemLimitType);
            limitData.setOverloadConfigValue(overloadConfigValue);
            limitData.setOverloadValue(overloadValue);
        } else if (e instanceof OverloadFlowException) {
            limitData = ((OverloadFlowException) e).getLimitData();
        }
        return limitData;
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        try {
            fireExit(context, resourceWrapper, count, args);
        } catch (Throwable e) {
            RecordLog.warn("Unexpected entry exit exception", e);
        }
    }
}
