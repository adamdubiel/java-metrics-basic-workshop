package com.adamdubiel.workshop.metrics.solutions.ex1;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Configuration
public class MetricsConfiguration {

    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Configuration
    static class MetricsReporterConfguration {

        @Autowired
        MetricRegistry metricRegistry;

        @PostConstruct
        public void startConsoleReporter() {
            ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).build();
            reporter.start(1, TimeUnit.MINUTES);
        }

    }
}
