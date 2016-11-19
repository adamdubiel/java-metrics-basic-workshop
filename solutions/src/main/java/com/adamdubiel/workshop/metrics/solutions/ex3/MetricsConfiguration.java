package com.adamdubiel.workshop.metrics.solutions.ex3;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

        String hostname = hostname();
        Graphite graphite = new Graphite("192.168.99.100", 2003);
        GraphiteReporter graphiteReporter = GraphiteReporter
                .forRegistry(metricRegistry)
                .prefixedWith("services.lunchbox." + hostname)
                .build(graphite);
        graphiteReporter.start(10, TimeUnit.SECONDS);
    }

    private String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Unable to read host name");
        }
    }
}
