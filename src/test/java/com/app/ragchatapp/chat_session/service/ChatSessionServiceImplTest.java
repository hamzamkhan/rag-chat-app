package com.app.ragchatapp.chat_session.service;

import com.app.ragchatapp.chat_session.model.dto.request.CreateChatSessionRequestDTO;
import com.app.ragchatapp.chat_session.model.dto.response.ChatSessionResponseDTO;
import com.app.ragchatapp.chat_session.model.entity.ChatSession;
import com.app.ragchatapp.chat_session.model.repo.ChatSessionRepository;
import com.app.ragchatapp.chat_session.service.impl.ChatSessionServiceImpl;
import com.app.ragchatapp.exception.CustomApiException;
import com.app.ragchatapp.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatSessionServiceImplTest {

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @InjectMocks
    private ChatSessionServiceImpl chatSessionService;

    @Test
    void createChatSession_throwsWhenSessionAlreadyExistsForUser() {
        UUID userId = UUID.randomUUID();
        CreateChatSessionRequestDTO request = CreateChatSessionRequestDTO.builder()
                .userId(userId)
                .sessionTitle("My Session")
                .favorite(false)
                .build();

        when(chatSessionRepository.existsByUserId(userId)).thenReturn(true);

        assertThrows(CustomApiException.class, () -> chatSessionService.createChatSession(request));
        verify(chatSessionRepository, never()).save(any(ChatSession.class));
    }

    @Test
    void createChatSession_savesAndReturnsResponse() {
        UUID userId = UUID.randomUUID();
        CreateChatSessionRequestDTO request = CreateChatSessionRequestDTO.builder()
                .userId(userId)
                .sessionTitle("Session Title")
                .favorite(true)
                .build();

        when(chatSessionRepository.existsByUserId(userId)).thenReturn(false);

        ChatSession saved = ChatSession.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .title("Session Title")
                .favorite(true)
                .build();

        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(saved);

        ChatSessionResponseDTO response = chatSessionService.createChatSession(request);

        assertNotNull(response);
        assertEquals(userId.toString(), response.getUserId());
        assertEquals("Session Title", response.getTitle());
        assertTrue(response.isFavorite());
    }

    @Test
    void getChatSession_returnsMappedResponse() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ChatSession entity = ChatSession.builder()
                .id(id)
                .userId(userId)
                .title("Existing Session")
                .favorite(false)
                .build();

        when(chatSessionRepository.findById(id)).thenReturn(Optional.of(entity));

        ChatSessionResponseDTO response = chatSessionService.getChatSession(id.toString());

        assertEquals(id, response.getId());
        assertEquals(userId.toString(), response.getUserId());
        assertEquals("Existing Session", response.getTitle());
        assertFalse(response.isFavorite());
    }

    @Test
    void getChatSession_throwsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(chatSessionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> chatSessionService.getChatSession(id.toString()));
    }

    @Test
    void rename_updatesTitle() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ChatSession existing = ChatSession.builder()
                .id(id)
                .userId(userId)
                .title("Old")
                .favorite(false)
                .build();

        when(chatSessionRepository.findById(id)).thenReturn(Optional.of(existing));
        when(chatSessionRepository.save(any(ChatSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatSessionResponseDTO response = chatSessionService.rename(id, "New Title");

        assertEquals("New Title", response.getTitle());
        verify(chatSessionRepository).save(existing);
    }

    @Test
    void setFavorite_updatesFavoriteFlag() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ChatSession existing = ChatSession.builder()
                .id(id)
                .userId(userId)
                .title("Session")
                .favorite(false)
                .build();

        when(chatSessionRepository.findById(id)).thenReturn(Optional.of(existing));
        when(chatSessionRepository.save(any(ChatSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatSessionResponseDTO response = chatSessionService.setFavorite(id, true);

        assertTrue(response.isFavorite());
        verify(chatSessionRepository).save(existing);
    }

    @Test
    void deleteSession_deletesEntity() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ChatSession existing = ChatSession.builder()
                .id(id)
                .userId(userId)
                .title("Session")
                .favorite(false)
                .build();

        when(chatSessionRepository.findById(id)).thenReturn(Optional.of(existing));

        chatSessionService.deleteSession(id);

        verify(chatSessionRepository).delete(existing);
    }
}
