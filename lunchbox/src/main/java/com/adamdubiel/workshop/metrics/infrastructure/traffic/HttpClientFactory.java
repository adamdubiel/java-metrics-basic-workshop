package com.adamdubiel.workshop.metrics.infrastructure.traffic;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Component;

@Component
public class HttpClientFactory {

    public HttpClient client(int connectTimeout, int requestTimeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(requestTimeout)
                .build();

        HttpClientBuilder clientBuilder = HttpClients.custom()
                .setConnectionManager(connectionManager())
                .setDefaultRequestConfig(requestConfig);

        return clientBuilder.build();
    }

    PoolingHttpClientConnectionManager connectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(1000);
        manager.setDefaultMaxPerRoute(1000);

        // measuring code goes here :)

        return manager;
    }

}
