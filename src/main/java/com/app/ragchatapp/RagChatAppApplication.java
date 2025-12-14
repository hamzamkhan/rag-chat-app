package com.app.ragchatapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.app.ragchatapp",
        "com.app.ragchatapp.chat_message",
        "com.app.ragchatapp.chat_session",
        "com.app.ragchatapp.user",
        "com.app.ragchatapp.aop",
        "com.app.ragchatapp.exception",
        "com.app.ragchatapp.config"
})
@EntityScan(basePackages = {
        "com.app.ragchatapp.chat_message.model.entity",
        "com.app.ragchatapp.chat_session.model.entity",
        "com.app.ragchatapp.user.model.entity",
})
@EnableJpaRepositories(basePackages = {
        "com.app.ragchatapp.chat_message.model.repo",
        "com.app.ragchatapp.chat_session.model.repo",
        "com.app.ragchatapp.user.model.repo",
})
public class RagChatAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagChatAppApplication.class, args);
    }

}
