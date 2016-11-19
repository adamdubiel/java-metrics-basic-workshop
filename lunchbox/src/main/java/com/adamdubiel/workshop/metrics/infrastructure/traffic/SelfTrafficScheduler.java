package com.adamdubiel.workshop.metrics.infrastructure.traffic;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class SelfTrafficScheduler implements AutoCloseable {

    private final RestTemplate restTemplate;

    private final ScheduledExecutorService scheduler;

    public SelfTrafficScheduler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.scheduler = Executors.newScheduledThreadPool(30);
    }

    public void start(Map<URI, Integer> endpointsToQuery) {
        endpointsToQuery.forEach((uri, frequency) ->
                scheduler.scheduleAtFixedRate(new SelfTrafficGenerator(uri, restTemplate), 100, 1000 / frequency, TimeUnit.MILLISECONDS)
        );
    }

    @Override
    public void close() throws Exception {
        scheduler.shutdown();
        scheduler.awaitTermination(1, TimeUnit.SECONDS);
    }
}
