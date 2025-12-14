package com.app.ragchatapp.chat_message.controller;

import com.app.ragchatapp.chat_message.model.dto.request.CreateChatMessageRequestDTO;
import com.app.ragchatapp.chat_message.model.dto.response.ChatMessageResponseDTO;
import com.app.ragchatapp.chat_message.service.ChatMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/message")
@Tag(name = "Chat Messages")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @PostMapping
    public ResponseEntity<ChatMessageResponseDTO> addMessage(
            @Valid @RequestBody CreateChatMessageRequestDTO request) {
        return ResponseEntity.ok(chatMessageService.addMessage(request));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<Page<ChatMessageResponseDTO>> getMessages(
            @PathVariable UUID sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        return ResponseEntity.ok(chatMessageService.getMessages(sessionId, pageable));
    }
}
