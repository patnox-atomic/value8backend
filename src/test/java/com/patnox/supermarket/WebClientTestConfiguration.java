package com.patnox.supermarket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

@TestConfiguration
@Slf4j
public class WebClientTestConfiguration 
{

    @Bean
    public WebClient getWebClient(final WebClient.Builder builder) {
        WebClient webClient = builder
                .baseUrl("http://localhost")
                .build();
        log.info("WebClient Instance Created During Testing: {}", webClient);
        return webClient;
    }
    
}
