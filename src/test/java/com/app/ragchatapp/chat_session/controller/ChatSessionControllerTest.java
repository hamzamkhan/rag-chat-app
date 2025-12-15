package com.app.ragchatapp.chat_session.controller;

import com.app.ragchatapp.chat_session.model.dto.request.CreateChatSessionRequestDTO;
import com.app.ragchatapp.chat_session.model.dto.response.ChatSessionResponseDTO;
import com.app.ragchatapp.chat_session.service.ChatSessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatSessionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChatSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatSessionService chatSessionService;

    @Test
    void createSession_returnsOk() throws Exception {
        UUID userId = UUID.randomUUID();
        ChatSessionResponseDTO response = ChatSessionResponseDTO.builder()
                .id(UUID.randomUUID())
                .userId(userId.toString())
                .title("My Session")
                .favorite(true)
                .build();

        when(chatSessionService.createChatSession(any(CreateChatSessionRequestDTO.class))).thenReturn(response);

        String body = "{" +
                "\"userId\":\"" + userId + "\"," +
                "\"sessionTitle\":\"My Session\"," +
                "\"favorite\":true" +
                "}";

        mockMvc.perform(post("/v1/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("My Session")))
                .andExpect(jsonPath("$.favorite", is(true)));
    }

    @Test
    void rename_updatesTitle() throws Exception {
        UUID id = UUID.randomUUID();
        ChatSessionResponseDTO response = ChatSessionResponseDTO.builder()
                .id(id)
                .userId(UUID.randomUUID().toString())
                .title("Renamed")
                .favorite(false)
                .build();

        when(chatSessionService.rename(any(UUID.class), any(String.class))).thenReturn(response);

        mockMvc.perform(patch("/v1/session/" + id + "/rename")
                        .param("title", "Renamed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Renamed")));
    }

    @Test
    void favorite_updatesFlag() throws Exception {
        UUID id = UUID.randomUUID();
        ChatSessionResponseDTO response = ChatSessionResponseDTO.builder()
                .id(id)
                .userId(UUID.randomUUID().toString())
                .title("Session")
                .favorite(true)
                .build();

        when(chatSessionService.setFavorite(any(UUID.class), any(Boolean.class))).thenReturn(response);

        mockMvc.perform(patch("/v1/session/" + id + "/favorite")
                        .param("favorite", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorite", is(true)));
    }

    @Test
    void deleteSession_returnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/v1/session/" + id))
                .andExpect(status().isNoContent());
    }
}
