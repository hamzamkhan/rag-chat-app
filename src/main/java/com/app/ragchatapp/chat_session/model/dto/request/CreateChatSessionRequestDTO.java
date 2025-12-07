package com.app.ragchatapp.chat_session.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatSessionRequestDTO implements Serializable {
    private UUID userId;
    private String sessionTitle;
    private boolean favorite = false;
}
