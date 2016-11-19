package com.adamdubiel.workshop.metrics.solutions.ex5;

import com.codahale.metrics.CachedGauge;
import com.codahale.metrics.DerivativeGauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

public class ThreadPoolMetrics {

    private static final int METRICS_CACHE_SECONDS = 20;

    private static final String THREAD_POOLS_PREFIX = "thread-pools";

    private static final String ACTIVE_THREADS = "active-threads";

    private static final String CAPACITY = "capacity";

    private static final String UTILIZATION = "utilization";

    public static MetricSet createGauges(String threadPoolName, ScheduledThreadPoolExecutor executor) {
        CachedGauge<Properties> cachedGauge = new CachedGauge<Properties>(METRICS_CACHE_SECONDS, TimeUnit.SECONDS) {
            @Override
            protected Properties loadValue() {
                return new Properties(executor);
            }
        };

        return () -> {
            Map<String, Metric> metrics = new HashMap<>();
            metrics.put(name(THREAD_POOLS_PREFIX, threadPoolName, CAPACITY), new DerivativeGauge<Properties, Integer>(cachedGauge) {
                @Override
                protected Integer transform(Properties value) {
                    return value.threadPoolSize;
                }
            });
            metrics.put(name(THREAD_POOLS_PREFIX, threadPoolName, ACTIVE_THREADS), new DerivativeGauge<Properties, Integer>(cachedGauge) {
                @Override
                protected Integer transform(Properties value) {
                    return value.activeThreads;
                }
            });
            metrics.put(name(THREAD_POOLS_PREFIX, threadPoolName, UTILIZATION), new DerivativeGauge<Properties, Double>(cachedGauge) {
                @Override
                protected Double transform(Properties value) {
                    return value.threadPoolUtilization;
                }
            });
            return metrics;
        };
    }

    private static class Properties {

        private final int threadPoolSize;

        private final int activeThreads;

        private final double threadPoolUtilization;

        Properties(ScheduledThreadPoolExecutor executor) {
            this.threadPoolSize = executor.getPoolSize();
            this.activeThreads = executor.getActiveCount();
            this.threadPoolUtilization = (double) activeThreads / (double) threadPoolSize;
        }
    }
}

