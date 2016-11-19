package com.adamdubiel.workshop.metrics.infrastructure.traffic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class SelfTrafficGenerator implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SelfTrafficGenerator.class);

    private final URI endpoint;

    private final RestTemplate restTemplate;

    public SelfTrafficGenerator(URI endpoint, RestTemplate restTemplate) {
        this.endpoint = endpoint;
        this.restTemplate = restTemplate;
    }

    private void callEndpoint() {
        restTemplate.getForObject(endpoint, String.class);
    }

    @Override
    public void run() {
        try {
            callEndpoint();
        } catch (Exception e) {
            logger.error("Unable to finalize HTTP request", e);
        }
    }
}
