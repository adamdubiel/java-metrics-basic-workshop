package com.adamdubiel.workshop.metrics.infrastructure.traffic;

import com.codahale.metrics.MetricSet;

import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadPoolMetrics {

    public static MetricSet createGauges(String threadPoolName, ScheduledThreadPoolExecutor executor) {
        return () -> new HashMap<>();
    }

}
