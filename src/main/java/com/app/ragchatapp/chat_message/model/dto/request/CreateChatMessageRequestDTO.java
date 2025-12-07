package com.app.ragchatapp.chat_message.model.dto.request;

import com.app.ragchatapp.chat_message.model.dto.enums.OriginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateChatMessageRequestDTO implements Serializable {
    private String userId;
    private String message;
    private String sessionId;
    private OriginType origin;
    private List<RagContextChunkDto> contextChunks;
}
