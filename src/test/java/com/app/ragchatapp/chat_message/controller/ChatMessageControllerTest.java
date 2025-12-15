package com.app.ragchatapp.chat_message.controller;

import com.app.ragchatapp.chat_message.model.dto.enums.OriginType;
import com.app.ragchatapp.chat_message.model.dto.request.CreateChatMessageRequestDTO;
import com.app.ragchatapp.chat_message.model.dto.response.ChatMessageResponseDTO;
import com.app.ragchatapp.chat_message.service.ChatMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatMessageController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChatMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessageService chatMessageService;

    @Test
    void addMessage_returnsCreatedMessage() throws Exception {
        ChatMessageResponseDTO response = ChatMessageResponseDTO.builder()
                .id(1L)
                .message("hello")
                .originType(OriginType.USER)
                .build();

        when(chatMessageService.addMessage(any(CreateChatMessageRequestDTO.class))).thenReturn(response);

        String body = "{" +
                "\"userId\":\"" + UUID.randomUUID() + "\"," +
                "\"sessionId\":\"" + UUID.randomUUID() + "\"," +
                "\"origin\":\"USER\"," +
                "\"message\":\"hello\"" +
                "}";

        mockMvc.perform(post("/v1/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.message", is("hello")));
    }

    @Test
    void getMessages_returnsPageOfMessages() throws Exception {
        UUID sessionId = UUID.randomUUID();
        ChatMessageResponseDTO dto = ChatMessageResponseDTO.builder()
                .id(5L)
                .message("hi")
                .originType(OriginType.BOT)
                .build();

        Page<ChatMessageResponseDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 50), 1);
        when(chatMessageService.getMessages(any(UUID.class), any())).thenReturn(page);

        mockMvc.perform(get("/v1/message/" + sessionId)
                        .param("page", "0")
                        .param("size", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(5)))
                .andExpect(jsonPath("$.content[0].message", is("hi")));
    }
}
