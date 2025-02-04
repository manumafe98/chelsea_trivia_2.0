package com.manumafe.players.data.playersdata.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    
    @Value("${ALLOWED_ORIGINS_HEADERS}")
    private String allowdOrigins;

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(allowdOrigins)
            .allowedMethods("GET")
            .allowedHeaders("api-key");
    }
}
