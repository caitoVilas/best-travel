package com.bestTravel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Configuration
public class WebClientConfig {

    @Value("${api.base.url}")
    private String baseUrl;
    @Value("${api.api-key}")
    private String apiKey;
    @Value("${api.api-key.header}")
    private String apiKeyHeader;

    @Bean("currency")
    public WebClient currencyWebClient(){
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(apiKeyHeader, apiKey)
                .build();
    }

    @Bean("base")
    public WebClient baseWebClient(){
        return WebClient.builder()
                .baseUrl("")
                .defaultHeader(apiKeyHeader, apiKey)
                .build();
    }
}
