package com.app.ragchatapp.chat_session.service.impl;

import com.app.ragchatapp.chat_session.model.dto.request.CreateChatSessionRequestDTO;
import com.app.ragchatapp.chat_session.model.dto.response.ChatSessionResponseDTO;
import com.app.ragchatapp.chat_session.model.entity.ChatSession;
import com.app.ragchatapp.chat_session.model.repo.ChatSessionRepository;
import com.app.ragchatapp.chat_session.service.ChatSessionService;
import com.app.ragchatapp.exception.CustomApiException;
import com.app.ragchatapp.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatSessionServiceImpl implements ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;

    @Transactional
    @Override
    public ChatSessionResponseDTO createChatSession(CreateChatSessionRequestDTO createChatSessionRequestDTO) {
        if (chatSessionRepository.existsByUserId(createChatSessionRequestDTO.getUserId())) {
            throw new CustomApiException("Session already exists for user: "
                    + createChatSessionRequestDTO.getUserId());
        }

        ChatSession chatSession = ChatSession.builder()
                .userId(createChatSessionRequestDTO.getUserId())
                .favorite(createChatSessionRequestDTO.isFavorite())
                .title(createChatSessionRequestDTO.getSessionTitle())
                .build();
        chatSessionRepository.save(chatSession);

        return ChatSessionResponseDTO.builder()
                .userId(String.valueOf(chatSession.getUserId()))
                .favorite(chatSession.isFavorite())
                .title(chatSession.getTitle())
                .build();
    }

    @Override
    public ChatSessionResponseDTO getChatSession(String chatSessionId) {
        return toResponse(chatSessionRepository.findById(UUID.fromString(chatSessionId))
                .orElseThrow(() -> new NotFoundException("Chat session not found: " + chatSessionId)));
    }

    @Override
    public ChatSession getEntity(String chatSessionId) {
        return chatSessionRepository.findById(UUID.fromString(chatSessionId))
                .orElseThrow(() -> new NotFoundException("Chat session not found: " + chatSessionId));
    }

    @Transactional
    @Override
    public ChatSessionResponseDTO rename(UUID id, String newTitle) {
        ChatSession session = getEntity(String.valueOf(id));
        session.setTitle(newTitle);
        return toResponse(chatSessionRepository.save(session));
    }

    @Transactional
    @Override
    public ChatSessionResponseDTO setFavorite(UUID id, boolean favorite) {
        ChatSession session = getEntity(String.valueOf(id));
        session.setFavorite(favorite);
        return toResponse(chatSessionRepository.save(session));
    }

    @Transactional
    @Override
    public void deleteSession(UUID id) {
        ChatSession session = getEntity(String.valueOf(id));
        chatSessionRepository.delete(session); // cascade messages if needed
    }

    private ChatSessionResponseDTO toResponse(ChatSession s) {
        ChatSessionResponseDTO dto = new ChatSessionResponseDTO();
        dto.setId(s.getId());
        dto.setUserId(String.valueOf(s.getUserId()));
        dto.setTitle(s.getTitle());
        dto.setFavorite(s.isFavorite());
        return dto;
    }
}
