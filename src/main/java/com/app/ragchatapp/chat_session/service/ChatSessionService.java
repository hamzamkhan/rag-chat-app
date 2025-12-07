package com.app.ragchatapp.chat_session.service;

import com.app.ragchatapp.chat_session.model.dto.request.CreateChatSessionRequestDTO;
import com.app.ragchatapp.chat_session.model.dto.response.ChatSessionResponseDTO;
import com.app.ragchatapp.chat_session.model.entity.ChatSession;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface ChatSessionService {
    ChatSessionResponseDTO createChatSession(CreateChatSessionRequestDTO createChatSessionRequestDTO);
    ChatSessionResponseDTO getChatSession(String chatSessionId);
    ChatSession getEntity(String chatSessionId);
    ChatSessionResponseDTO rename(UUID id, String title);
    ChatSessionResponseDTO setFavorite(UUID id, boolean favorite);
    void deleteSession(UUID id);
}
