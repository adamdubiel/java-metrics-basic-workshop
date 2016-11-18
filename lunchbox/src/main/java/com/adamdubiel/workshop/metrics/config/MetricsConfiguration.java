package com.adamdubiel.workshop.metrics.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MetricsConfiguration {

    @Configuration
    static class MetricsReporterConfguration {

        @PostConstruct
        public void startConsoleReporter() {
        }

    }
}
