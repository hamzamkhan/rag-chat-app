package com.app.ragchatapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI ragChatOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RAG Chat App")
                        .description("Microservice for storing chat sessions and messages")
                        .version("v1"));
    }
}
