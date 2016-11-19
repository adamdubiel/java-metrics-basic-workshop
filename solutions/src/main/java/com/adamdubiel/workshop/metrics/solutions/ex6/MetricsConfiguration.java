package com.adamdubiel.workshop.metrics.solutions.ex6;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
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

        registerAll("gc", new GarbageCollectorMetricSet(), metricRegistry);
        registerAll("memory", new MemoryUsageGaugeSet(), metricRegistry);
    }

    private String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Unable to read host name");
        }
    }

    private void registerAll(String prefix, MetricSet metricSet, MetricRegistry registry) {
        for (Map.Entry<String, Metric> entry : metricSet.getMetrics().entrySet()) {
            if (entry.getValue() instanceof MetricSet) {
                registerAll(prefix + "." + entry.getKey(), (MetricSet) entry.getValue(), registry);
            } else {
                registry.register(prefix + "." + entry.getKey(), entry.getValue());
            }
        }
    }
}
