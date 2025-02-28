package com.idea.springboot.webflux.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${config.base.endpoint}")
    private String uri;

    @Bean
    public WebClient registerWebClient() {
        return WebClient.create(uri);
    }
}
