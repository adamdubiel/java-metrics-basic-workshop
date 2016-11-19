package com.adamdubiel.workshop.metrics.solutions.ex2;

import com.adamdubiel.workshop.metrics.api.MetricsInterceptor;
import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class FiltersConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    MetricRegistry metricRegistry;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MetricsInterceptor(metricRegistry));
    }
}
