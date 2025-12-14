package com.app.ragchatapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiKeyConfig {

    @Value("${ragchat.api-key}")
    private String expectedApiKey;

    public String getExpectedApiKey() {
        return expectedApiKey;
    }
}
