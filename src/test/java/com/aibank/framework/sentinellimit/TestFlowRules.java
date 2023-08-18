package com.aibank.framework.sentinellimit;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.RandomUtil;
import com.aibank.framework.sentinellimit.rule.GlobalOverloadConfig;
import com.aibank.framework.sentinellimit.rule.OverloadFlowRuleManager;
import com.aibank.framework.sentinellimit.service.DefaultFlowLimitLoad;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.concurrent.NamedThreadFactory;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.node.SampleCountProperty;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class TestFlowRules {

    public static DataSource createDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sentinel");
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

    private AtomicLong cost = new AtomicLong(0);
    private AtomicLong count = new AtomicLong(0);

    @Test
    public void testFlowRules() throws InterruptedException {
        DataSource dataSource = createDataSource();
        // initFlowRules();
        DefaultFlowLimitLoad flowLimitService = new DefaultFlowLimitLoad(dataSource, "237000");
        flowLimitService.init();
        //  SampleCountProperty.SAMPLE_COUNT = 1;

        //  ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(50, new NamedThreadFactory("test-task", true));

        for (int i = 0; i < 20; i++) {
            new Thread(runnable).start();
        }
        new CountDownLatch(1).await();

    }

    Runnable runnable = () -> {
        while (true) {
            StopWatch stopWatch = StopWatch.create("1");
            stopWatch.start();
            try {
                accessHelloWorld();

                stopWatch.stop();
                //  System.out.println(" 调用耗时 " + (stopWatch.getTotalTimeMillis() - 1000l));
                long l = cost.addAndGet(stopWatch.getTotalTimeMillis() - 1000) / count.incrementAndGet();
             //   System.out.println(" 平均睡眠 " + l);
            } catch (BlockException e) {
                e.printStackTrace();
                // throw new RuntimeException(e);
            }
        }
    };

    @Test
    public void testSystemRules() throws InterruptedException {
        initSystemRules();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    accessHelloWorld();
                } catch (BlockException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        new CountDownLatch(1).await();
    }


    public void loadSystemOverloadConfig() {
        SystemRule systemRule = new SystemRule();

        // max load is 3
        systemRule.setHighestSystemLoad(1.0);
        // max cpu usage is 60%
        systemRule.setHighestCpuUsage(0.4);
        // max avg rt of all request is 10 ms
        systemRule.setAvgRt(10);
        // max total qps is 20
        systemRule.setQps(5);
        // max parallel working thread is 10
        systemRule.setMaxThread(10);

        GlobalOverloadConfig.loadConfig(systemRule);
    }


    private void initSystemRules() {
        SystemRule rule = new SystemRule();
        // max load is 3
        rule.setHighestSystemLoad(1.0);
        // max cpu usage is 60%
        rule.setHighestCpuUsage(0.4);
        // max avg rt of all request is 10 ms
        rule.setAvgRt(10);
        // max total qps is 20
        rule.setQps(50);
        // max parallel working thread is 10
        rule.setMaxThread(10);

        SystemRuleManager.loadRules(Collections.singletonList(rule));
    }

    private void initFlowRules() {
        List<FlowRule> flowRules = new ArrayList<>();
        FlowRule flowRule = new FlowRule();

        // 指定限流规则的Resource
        flowRule.setResource("HelloWorld");
        flowRule.setLimitApp("237001");

        // 是否启用Cluster模式
        flowRule.setClusterMode(false);
        // 默认限流规则，具体可以看FlowSlot介绍
        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        // 总数
        flowRule.setCount(5);
        // 策略
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        // 限制QPS 也可以指定为线程数
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRules.add(flowRule);
        // 加载配置
        OverloadFlowRuleManager.loadRules(flowRules);
    }

    private static void accessHelloWorld() throws BlockException {
        Entry entry = null;
        try {
            ContextUtil.enter("OPT", "237001");
            entry = SphU.entry("HelloWorld", EntryType.IN);

            if (entry != null) {
                System.out.println(new Date() + " acquire success");
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            } else {
                System.out.println(new Date() + " acquire fail null");
            }

        } catch (BlockException e) {
            System.out.println(new Date() + " acquire fail");
            throw e;
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

}
