package com.aibank.framework.sentinellimit.service;

import cn.hutool.core.bean.BeanUtil;
import com.aibank.framework.sentinellimit.dao.*;
import com.aibank.framework.sentinellimit.dao.entity.FlowRuleEntity;
import com.aibank.framework.sentinellimit.dao.entity.SystemRuleEntity;
import com.aibank.framework.sentinellimit.dao.impl.BlockInfoRecordMapperImpl;
import com.aibank.framework.sentinellimit.dao.impl.FlowRuleMapperImpl;
import com.aibank.framework.sentinellimit.dao.impl.SystemRuleMapperImpl;
import com.aibank.framework.sentinellimit.datasource.JdbcDataSource;
import com.aibank.framework.sentinellimit.domain.LimitConstants;
import com.aibank.framework.sentinellimit.rule.GlobalOverloadConfig;
import com.aibank.framework.sentinellimit.rule.OverloadFlowRuleManager;
import com.aibank.framework.sentinellimit.task.MetricPrintTask;
import com.aibank.framework.sentinellimit.task.RecordsBlockTask;
import com.alibaba.csp.sentinel.concurrent.NamedThreadFactory;
import com.alibaba.csp.sentinel.datasource.*;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DefaultFlowLimitLoad implements FlowLimitLoad {

    private final ScheduledExecutorService executorService;
    private BlockInfoRecordMapper blockInfoRecordMapper;
    private FlowRuleMapper flowRuleMapper;
    private SystemRuleMapper systemRuleMapper;


    public DefaultFlowLimitLoad(DataSource dataSource, String app) {
        LimitConstants.app = app;
        executorService = new ScheduledThreadPoolExecutor(5, new NamedThreadFactory("sentinel-record-task", true));
        blockInfoRecordMapper = new BlockInfoRecordMapperImpl(dataSource);
        flowRuleMapper = new FlowRuleMapperImpl(dataSource);
        systemRuleMapper = new SystemRuleMapperImpl(dataSource);
    }

    public void init() {
        assert executorService != null;
        assert blockInfoRecordMapper != null;
        assert flowRuleMapper != null;
        assert systemRuleMapper != null;

        // 正常限流
        flowRule();
        // 系统高负荷限流
        overloadFlowRule();
        //系统负载限流
        systemRule();
        // 启动定时任务
        scheduleTask();

    }

    protected void scheduleTask() {
        executorService.scheduleWithFixedDelay(new RecordsBlockTask(blockInfoRecordMapper), 0, 1, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new MetricPrintTask(), 0, 1, TimeUnit.SECONDS);
    }

    public void close() {
        executorService.shutdown();
    }


    @Override
    public void flowRule() {
        ReadableDataSource<List<FlowRuleEntity>, List<FlowRule>> readableDataSource = new JdbcDataSource<>(flowRuleMapper::getAllFlowRule, source -> source.stream().map(s -> {
            FlowRule flowRule = new FlowRule();
            BeanUtil.copyProperties(s, flowRule);
            return flowRule;
        }).collect(Collectors.toList()));
        FlowRuleManager.register2Property(readableDataSource.getProperty());
    }

    @Override
    public void overloadFlowRule() {
        ReadableDataSource<List<FlowRuleEntity>, List<FlowRule>> readableDataSource = new JdbcDataSource<>(flowRuleMapper::getAllOverloadFlowRule, source -> source.stream().map(s -> {
            FlowRule flowRule = new FlowRule();
            BeanUtil.copyProperties(s, flowRule);
            return flowRule;
        }).collect(Collectors.toList()));
        OverloadFlowRuleManager.register2Property(readableDataSource.getProperty());
    }

    @Override
    public void systemRule() {
        ReadableDataSource<List<SystemRuleEntity>, List<SystemRule>> readableDataSource = new JdbcDataSource<>(systemRuleMapper::getAllRule, source -> source.stream().map(s -> {
            SystemRule systemRule = new SystemRule();
            BeanUtil.copyProperties(s, systemRule);
            if (s.getSystemOverloadFlag()) {
                GlobalOverloadConfig.loadConfig(systemRule);
                return null;
            } else {
                return systemRule;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList()));
        SystemRuleManager.register2Property(readableDataSource.getProperty());
    }

    public void setBlockInfoRecordMapper(BlockInfoRecordMapper blockInfoRecordMapper) {
        this.blockInfoRecordMapper = blockInfoRecordMapper;
    }

    public void setFlowRuleMapper(FlowRuleMapper flowRuleMapper) {
        this.flowRuleMapper = flowRuleMapper;
    }

    public void setSystemRuleMapper(SystemRuleMapper systemRuleMapper) {
        this.systemRuleMapper = systemRuleMapper;
    }
}
