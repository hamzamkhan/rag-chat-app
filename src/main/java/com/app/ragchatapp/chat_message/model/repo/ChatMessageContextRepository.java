package com.app.ragchatapp.chat_message.model.repo;

import com.app.ragchatapp.chat_message.model.entity.ChatMessageContext;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageContextRepository extends JpaRepository<ChatMessageContext, Long> {
}
