package com.aibank.framework.sentinellimit;

import cn.hutool.core.date.StopWatch;
import com.aibank.framework.sentinellimit.service.DefaultFlowLimitLoad;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class DatasourceFlowRulesTest {

    private DruidDataSource dataSource;

    @Before
    public void createDataSource() {
        dataSource = new DruidDataSource();
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
    }

    private AtomicLong cost = new AtomicLong(0);
    private AtomicLong count = new AtomicLong(0);

    @Test
    public void testFlowRules() throws InterruptedException {
        DefaultFlowLimitLoad flowLimitService = new DefaultFlowLimitLoad(dataSource, "237000");
        flowLimitService.init();

        for (int i = 0; i < 10; i++) {
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
