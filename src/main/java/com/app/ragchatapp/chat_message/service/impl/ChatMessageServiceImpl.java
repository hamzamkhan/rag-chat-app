package com.app.ragchatapp.chat_message.service.impl;

import com.app.ragchatapp.chat_message.model.dto.request.CreateChatMessageRequestDTO;
import com.app.ragchatapp.chat_message.model.dto.request.RagContextChunkDto;
import com.app.ragchatapp.chat_message.model.dto.response.ChatMessageResponseDTO;
import com.app.ragchatapp.chat_message.model.entity.ChatMessage;
import com.app.ragchatapp.chat_message.model.entity.ChatMessageContext;
import com.app.ragchatapp.chat_message.model.repo.ChatMessageRepository;
import com.app.ragchatapp.chat_message.service.ChatMessageService;
import com.app.ragchatapp.chat_session.model.entity.ChatSession;
import com.app.ragchatapp.chat_session.service.ChatSessionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionService chatSessionService;

    @Transactional
    @Override
    public ChatMessageResponseDTO addMessage(CreateChatMessageRequestDTO request) {
        ChatSession session = chatSessionService.getEntity(String.valueOf(request.getSessionId()));

        ChatMessage msg = new ChatMessage();
        msg.setChatSession(session);
        msg.setOriginType(request.getOrigin());
        msg.setMessage(request.getMessage());

        if (request.getContextChunks() != null && !request.getContextChunks().isEmpty()) {
            for (RagContextChunkDto dto : request.getContextChunks()) {
                ChatMessageContext ctx = new ChatMessageContext();
                ctx.setSource(dto.getSource());
                ctx.setDocumentId(dto.getDocumentId());
                ctx.setChunkId(dto.getChunkId());
                ctx.setScore(dto.getScore());
                ctx.setText(dto.getText());
                ctx.setUrl(dto.getUrl());

                // maintain both sides of the relationship
                msg.addContextChunk(ctx);
            }
        }

        ChatMessage saved = chatMessageRepository.save(msg);

        session.setUpdatedAt(Instant.now());

        return toResponse(saved);
    }

    @Transactional
    @Override
    public Page<ChatMessageResponseDTO> getMessages(UUID sessionId, Pageable pageable) {
        ChatSession session = chatSessionService.getEntity(String.valueOf(sessionId));
        return chatMessageRepository.findBySessionOrderByCreatedAtAsc(session, pageable)
                .map(this::toResponse);
    }

    private ChatMessageResponseDTO toResponse(ChatMessage msg) {
        ChatMessageResponseDTO res = new ChatMessageResponseDTO();
        res.setId(msg.getId());
        res.setOriginType(msg.getOriginType());
        res.setMessage(msg.getMessage());
//        res.setCreatedAt(msg.getCreatedAt());

        List<RagContextChunkDto> chunks = new ArrayList<>();
        if (msg.getContext() != null) {
            for (ChatMessageContext ctx : msg.getContext()) {
                RagContextChunkDto dto = new RagContextChunkDto();
                dto.setSource(ctx.getSource());
                dto.setDocumentId(ctx.getDocumentId());
                dto.setChunkId(ctx.getChunkId());
                dto.setScore(ctx.getScore());
                dto.setText(ctx.getText());
                dto.setUrl(ctx.getUrl());
                chunks.add(dto);
            }
        }
        res.setContextChunks(chunks);

        return res;
    }
}
