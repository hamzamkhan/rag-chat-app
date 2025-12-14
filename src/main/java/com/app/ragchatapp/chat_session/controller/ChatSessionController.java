package com.app.ragchatapp.chat_session.controller;

import com.app.ragchatapp.chat_session.model.dto.request.CreateChatSessionRequestDTO;
import com.app.ragchatapp.chat_session.model.dto.response.ChatSessionResponseDTO;
import com.app.ragchatapp.chat_session.service.ChatSessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/session")
@AllArgsConstructor
@Tag(name = "Chat Sessions")
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    @PostMapping
    public ResponseEntity<?> createSession(@Valid @RequestBody CreateChatSessionRequestDTO requestDTO) {
        return ResponseEntity.ok(chatSessionService.createChatSession(requestDTO));
    }

    @PatchMapping("/{id}/rename")
    public ResponseEntity<ChatSessionResponseDTO> rename(@PathVariable UUID id, @RequestParam String title) {
        return ResponseEntity.ok(chatSessionService.rename(id, title));
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<ChatSessionResponseDTO> favorite(
            @PathVariable UUID id,
            @RequestParam boolean favorite) {
        return ResponseEntity.ok(chatSessionService.setFavorite(id, favorite));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        chatSessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
