package com.app.ragchatapp.config;

import com.app.ragchatapp.chat_message.model.dto.enums.ChunkSource;
import com.app.ragchatapp.chat_message.model.dto.enums.OriginType;
import com.app.ragchatapp.chat_message.model.entity.ChatMessage;
import com.app.ragchatapp.chat_message.model.entity.ChatMessageContext;
import com.app.ragchatapp.chat_message.model.repo.ChatMessageRepository;
import com.app.ragchatapp.chat_session.model.entity.ChatSession;
import com.app.ragchatapp.chat_session.model.repo.ChatSessionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.UUID;

@Configuration
public class SeedDataConfig {

    @Bean
    public CommandLineRunner seedDemoData(ChatSessionRepository sessionRepo,
                                          ChatMessageRepository messageRepo) {
        return args -> {
            // Only seed if DB is empty
            if (sessionRepo.count() > 0) {
                return;
            }

            // 1) Create a demo session
            ChatSession session = new ChatSession();
            session.setUserId(UUID.fromString("2ee1d1c8-7179-47bc-b5dd-db98cd3b1e8e"));
            session.setTitle("Demo RAG Chat");
            session.setFavorite(true);
            session.setCreatedAt(Instant.now());
            session.setUpdatedAt(Instant.now());

            ChatSession savedSession = sessionRepo.save(session);

            // 2) Create a user question
            ChatMessage userMsg = new ChatMessage();
            userMsg.setChatSession(savedSession);
            userMsg.setOriginType(OriginType.USER);
            userMsg.setMessage("What is the UAE corporate tax rate?");
            // createdAt will be set by @PrePersist
            ChatMessage savedUserMsg = messageRepo.save(userMsg);

            // 3) Create an assistant answer with RAG context
            ChatMessage assistantMsg = new ChatMessage();
            assistantMsg.setChatSession(savedSession);
            assistantMsg.setOriginType(OriginType.BOT);
            assistantMsg.setMessage("The UAE corporate tax rate is 9% for taxable income above AED 375,000.");

            // Context chunk 1
            ChatMessageContext ctx1 = new ChatMessageContext();
            ctx1.setSource(ChunkSource.DB);
            ctx1.setDocumentId("uae-tax-law-2023");
            ctx1.setChunkId("chunk-18");
            ctx1.setScore(0.92);
            ctx1.setText("The standard Corporate Tax rate is 9% for taxable income exceeding AED 375,000...");
            ctx1.setUrl("https://mof.gov.ae/corporate-tax");

            // Context chunk 2
            ChatMessageContext ctx2 = new ChatMessageContext();
            ctx2.setSource(ChunkSource.SEARCH_INDEX);
            ctx2.setDocumentId("blog-uae-tax-guide");
            ctx2.setChunkId("chunk-4");
            ctx2.setScore(0.78);
            ctx2.setText("UAE introduced a 9% corporate tax rate starting June 2023...");
            ctx2.setUrl("https://example.com/uae-tax-guide");

            // Attach context chunks to assistant message
            assistantMsg.addContextChunk(ctx1);
            assistantMsg.addContextChunk(ctx2);

            ChatMessage savedAssistantMsg = messageRepo.save(assistantMsg);

            // Update session's updatedAt
            savedSession.setUpdatedAt(savedAssistantMsg.getCreatedAt());
            sessionRepo.save(savedSession);

            System.out.println("✅ Demo RAG data seeded into database");
        };
    }
}

