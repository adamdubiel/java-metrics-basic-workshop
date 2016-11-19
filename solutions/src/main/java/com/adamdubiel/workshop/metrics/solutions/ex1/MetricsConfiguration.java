package com.adamdubiel.workshop.metrics.solutions.ex1;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics
public class MetricsConfiguration extends MetricsConfigurerAdapter {

    @Override
    public MetricRegistry getMetricRegistry() {
        return new MetricRegistry();
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).build();
        reporter.start(10, TimeUnit.SECONDS);
    }
}
