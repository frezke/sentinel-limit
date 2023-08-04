
package com.aibank.framework.sentinellimit.datasource;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.alibaba.csp.sentinel.concurrent.NamedThreadFactory;
import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class JdbcDataSource<T> extends AbstractDataSource<List<Entity>, T> {
    private static Logger logger = LoggerFactory.getLogger(JdbcDataSource.class);
    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1, new NamedThreadFactory("sentinel-datasource-auto-refresh-task", true));
    protected static long recommendRefreshMs = 5000;

    private String getAllSql;
    private DataSource dataSource;


    public JdbcDataSource(DataSource dataSource, String getAllSql, Converter<List<Entity>, T> parser) {
        super(parser);
        AssertUtil.notNull(dataSource, "dataSource can not be empty");
        this.getAllSql = getAllSql;
        this.dataSource = dataSource;
        loadInitialConfig();
        startTimerService();
    }

    private void loadInitialConfig() {
        try {
            T newValue = loadConfig();
            if (newValue == null) {
                logger.warn("[RedisDataSource] WARN: initial config is null, you may have to check your data source");
            }
            getProperty().updateValue(newValue);
        } catch (Exception ex) {
            logger.warn("[RedisDataSource] Error when loading initial config", ex);
        }
    }

    @Override
    public List<Entity> readSource() {
        try {
            List<Entity> query = Db.use(dataSource).query(getAllSql);
            return query;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean isModified() {
        return true;
    }


    @SuppressWarnings("PMD.ThreadPoolCreationRule")
    private void startTimerService() {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isModified()) {
                        return;
                    }
                    T newValue = loadConfig();
                    getProperty().updateValue(newValue);
                } catch (Throwable e) {
                    logger.info("loadConfig exception", e);
                }
            }
        }, recommendRefreshMs, recommendRefreshMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() {
        if (service != null) {
            service.shutdownNow();
        }
    }
}
