
package com.aibank.framework.sentinellimit.datasource;

import com.alibaba.csp.sentinel.concurrent.NamedThreadFactory;
import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


public class CustomerDataSource<T> extends AbstractDataSource<T, T> {
    private static Logger logger = LoggerFactory.getLogger(CustomerDataSource.class);
    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1, new NamedThreadFactory("sentinel-datasource-auto-refresh-task", true));
    protected static long recommendRefreshMs = 3000;

    private Supplier<T> supplier;

    public CustomerDataSource(Supplier<T> supplier) {
        super(source -> source);
        this.supplier = supplier;
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
    public T readSource() {
        return supplier.get();
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
