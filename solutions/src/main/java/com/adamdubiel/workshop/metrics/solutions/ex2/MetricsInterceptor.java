package com.adamdubiel.workshop.metrics.solutions.ex2;

import com.codahale.metrics.MetricRegistry;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class MetricsInterceptor extends HandlerInterceptorAdapter {

    private static final String START_TIME_ATTRIBUTE = "metricsInterceptor_startTime";

    private final MetricRegistry metricRegistry;

    public MetricsInterceptor(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);

        if (startTime != null && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            metricRegistry.timer(createMetricName(handlerMethod)).update(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
        }
    }

    private String createMetricName(HandlerMethod method) {
        return method.getMethod().getDeclaringClass().getSimpleName() + "." + method.getMethod().getName();
    }
}
