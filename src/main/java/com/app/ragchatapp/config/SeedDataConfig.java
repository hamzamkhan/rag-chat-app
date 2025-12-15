package com.app.ragchatapp.config;

import com.app.ragchatapp.chat_message.model.dto.enums.ChunkSource;
import com.app.ragchatapp.chat_message.model.dto.enums.OriginType;
import com.app.ragchatapp.chat_message.model.entity.ChatMessage;
import com.app.ragchatapp.chat_message.model.entity.ChatMessageContext;
import com.app.ragchatapp.chat_message.model.repo.ChatMessageRepository;
import com.app.ragchatapp.chat_session.model.entity.ChatSession;
import com.app.ragchatapp.chat_session.model.repo.ChatSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner{

    private final ChatSessionRepository sessionRepo;
    private final ChatMessageRepository messageRepo;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            // demo-only: wipe and reinsert each startup
            messageRepo.deleteAll();
            sessionRepo.deleteAll();

            ChatSession session = new ChatSession();
            session.setUserId(UUID.fromString("a84101b1-0ddc-4bdd-bb60-fc9fd457c4aa"));
            session.setTitle("Demo RAG Chat");
            session.setFavorite(true);

            session = sessionRepo.save(session);
            log.info("✅ DataInitializer: created session {}", session.getId());

            ChatMessage userMsg = new ChatMessage();
            userMsg.setChatSession(session);
            userMsg.setOriginType(OriginType.USER);
            userMsg.setMessage("What is the UAE corporate tax rate?");
            userMsg = messageRepo.save(userMsg);
            log.info("✅ DataInitializer: created user msg {}", userMsg.getId());

            ChatMessage assistantMsg = new ChatMessage();
            assistantMsg.setChatSession(session);
            assistantMsg.setOriginType(OriginType.BOT);
            assistantMsg.setMessage("The UAE corporate tax rate is 9% for taxable income above AED 375,000.");

            ChatMessageContext ctx1 = new ChatMessageContext();
            ctx1.setSource(ChunkSource.DB);
            ctx1.setDocumentId("uae-tax-law-2023");
            ctx1.setChunkId("chunk-18");
            ctx1.setScore(0.92);
            ctx1.setText("The standard Corporate Tax rate is 9% for taxable income exceeding AED 375,000...");
            ctx1.setUrl("https://mof.gov.ae/corporate-tax");

            ChatMessageContext ctx2 = new ChatMessageContext();
            ctx2.setSource(ChunkSource.SEARCH_INDEX);
            ctx2.setDocumentId("blog-uae-tax-guide");
            ctx2.setChunkId("chunk-4");
            ctx2.setScore(0.78);
            ctx2.setText("UAE introduced a 9% corporate tax rate starting June 2023...");
            ctx2.setUrl("https://example.com/uae-tax-guide");

            assistantMsg.addContextChunk(ctx1);
            assistantMsg.addContextChunk(ctx2);

            assistantMsg = messageRepo.save(assistantMsg);
            log.info("✅ DataInitializer: created assistant msg {}", assistantMsg.getId());

            log.info("🎉 DataInitializer: seeding completed successfully");
        } catch (Exception e) {
            log.error("❌ DataInitializer: error while seeding DB", e);
            // let the app still start, it's a demo
        }
    }
}

