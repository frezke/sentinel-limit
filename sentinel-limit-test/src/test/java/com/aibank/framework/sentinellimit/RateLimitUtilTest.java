package com.aibank.framework.sentinellimit;

import cn.hutool.core.date.StopWatch;
import com.aibank.framework.sentinellimit.service.DefaultFlowLimitLoad;
import com.aibank.framework.sentinellimit.service.RateLimitUtil;
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

public class RateLimitUtilTest {

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

        for (int i = 0; i < 20; i++) {
            new Thread(runnable).start();
        }
        new CountDownLatch(1).await();

    }

    Runnable runnable = () -> {
        while (true) {
            StopWatch stopWatch = StopWatch.create("1");
            stopWatch.start();
            boolean success = RateLimitUtil.wait("HelloWorld",EntryType.IN,5.0);
            stopWatch.stop();
            System.out.println(" 调用耗时 " + (stopWatch.getTotalTimeMillis() - 1000l));
            //long l = cost.addAndGet(stopWatch.getTotalTimeMillis() - 1000) / count.incrementAndGet();
            //   System.out.println(" 平均睡眠 " + l);

            if (success) {
                System.out.println("success");
            } else {
                System.out.println("fail");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };
}
