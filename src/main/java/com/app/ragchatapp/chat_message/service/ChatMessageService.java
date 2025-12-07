package com.app.ragchatapp.chat_message.service;

import com.app.ragchatapp.chat_message.model.dto.request.CreateChatMessageRequestDTO;
import com.app.ragchatapp.chat_message.model.dto.response.ChatMessageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ChatMessageService {
    ChatMessageResponseDTO addMessage(CreateChatMessageRequestDTO createChatMessageRequestDTO);
    Page<ChatMessageResponseDTO> getMessages(UUID sessionId, Pageable pageable);
}
