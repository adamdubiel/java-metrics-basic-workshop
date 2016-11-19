package com.adamdubiel.workshop.metrics.config;

import com.adamdubiel.workshop.metrics.infrastructure.traffic.HttpClientFactory;
import com.adamdubiel.workshop.metrics.infrastructure.traffic.SelfTrafficScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TrafficGeneratorConfiguration {

    @Bean
    RestTemplate restTemplate(HttpClientFactory httpClientFactory) {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClientFactory.client(100, 1000)));
    }

    @Component
    private class ServerStartListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

        @Autowired
        SelfTrafficScheduler selfTrafficScheduler;

        @Override
        public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
            String prefix = "http://localhost:" + event.getEmbeddedServletContainer().getPort();

            Map<URI, Integer> endpointsToQuery = new HashMap<>();
            endpointsToQuery.put(URI.create(prefix + "/places"), 5);
            endpointsToQuery.put(URI.create(prefix + "/places/TukTuk/upvote"), 2);
            endpointsToQuery.put(URI.create(prefix + "/places/Fraza/downvote"), 1);

            selfTrafficScheduler.start(endpointsToQuery);
        }
    }
}
