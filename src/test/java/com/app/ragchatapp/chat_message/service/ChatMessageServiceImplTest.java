package com.app.ragchatapp.chat_message.service;

import com.app.ragchatapp.chat_message.model.dto.enums.ChunkSource;
import com.app.ragchatapp.chat_message.model.dto.enums.OriginType;
import com.app.ragchatapp.chat_message.model.dto.request.CreateChatMessageRequestDTO;
import com.app.ragchatapp.chat_message.model.dto.request.RagContextChunkDto;
import com.app.ragchatapp.chat_message.model.dto.response.ChatMessageResponseDTO;
import com.app.ragchatapp.chat_message.model.entity.ChatMessage;
import com.app.ragchatapp.chat_message.model.entity.ChatMessageContext;
import com.app.ragchatapp.chat_message.model.repo.ChatMessageRepository;
import com.app.ragchatapp.chat_message.service.impl.ChatMessageServiceImpl;
import com.app.ragchatapp.chat_session.model.entity.ChatSession;
import com.app.ragchatapp.chat_session.service.ChatSessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceImplTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatSessionService chatSessionService;

    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;

    @Test
    void addMessage_persistsMessageAndContext() {
        UUID sessionId = UUID.randomUUID();
        ChatSession session = ChatSession.builder().id(sessionId).build();

        RagContextChunkDto chunkDto = RagContextChunkDto.builder()
                .source(ChunkSource.DB)
                .documentId("doc-1")
                .chunkId("chunk-1")
                .score(0.9)
                .text("some text")
                .url("http://example.com")
                .build();

        CreateChatMessageRequestDTO request = CreateChatMessageRequestDTO.builder()
                .sessionId(sessionId.toString())
                .userId(UUID.randomUUID().toString())
                .origin(OriginType.USER)
                .message("hello")
                .contextChunks(List.of(chunkDto))
                .build();

        when(chatSessionService.getEntity(sessionId.toString())).thenReturn(session);

        ChatMessage saved = new ChatMessage();
        saved.setId(1L);
        saved.setChatSession(session);
        saved.setOriginType(OriginType.USER);
        saved.setMessage("hello");

        ChatMessageContext ctx = new ChatMessageContext();
        ctx.setSource(ChunkSource.DB);
        ctx.setDocumentId("doc-1");
        ctx.setChunkId("chunk-1");
        ctx.setScore(0.9);
        ctx.setText("some text");
        ctx.setUrl("http://example.com");
        saved.setContext(List.of(ctx));

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(saved);

        ChatMessageResponseDTO response = chatMessageService.addMessage(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("hello", response.getMessage());
        assertEquals(OriginType.USER, response.getOriginType());
        assertNotNull(response.getContextChunks());
        assertEquals(1, response.getContextChunks().size());
        RagContextChunkDto resChunk = response.getContextChunks().get(0);
        assertEquals("doc-1", resChunk.getDocumentId());
        assertEquals("chunk-1", resChunk.getChunkId());
    }

    @Test
    void getMessages_returnsMappedPage() {
        UUID sessionId = UUID.randomUUID();
        ChatSession session = ChatSession.builder().id(sessionId).build();

        when(chatSessionService.getEntity(sessionId.toString())).thenReturn(session);

        ChatMessage msg = new ChatMessage();
        msg.setId(10L);
        msg.setChatSession(session);
        msg.setOriginType(OriginType.BOT);
        msg.setMessage("response");

        Page<ChatMessage> page = new PageImpl<>(List.of(msg));
        when(chatMessageRepository.findByChatSessionOrderByCreatedAtAsc(any(ChatSession.class), any()))
                .thenReturn(page);

        Page<ChatMessageResponseDTO> result = chatMessageService.getMessages(sessionId, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        ChatMessageResponseDTO dto = result.getContent().get(0);
        assertEquals(10L, dto.getId());
        assertEquals("response", dto.getMessage());
        assertEquals(OriginType.BOT, dto.getOriginType());
    }
}
