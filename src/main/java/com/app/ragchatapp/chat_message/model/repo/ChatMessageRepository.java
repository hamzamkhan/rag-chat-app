package com.app.ragchatapp.chat_message.model.repo;

import com.app.ragchatapp.chat_message.model.entity.ChatMessage;
import com.app.ragchatapp.chat_session.model.entity.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findBySessionOrderByCreatedAtAsc(ChatSession session, Pageable pageable);
}
