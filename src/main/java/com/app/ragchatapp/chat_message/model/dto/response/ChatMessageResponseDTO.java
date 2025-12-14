package com.app.ragchatapp.chat_message.model.dto.response;

import com.app.ragchatapp.chat_message.model.dto.enums.OriginType;
import com.app.ragchatapp.chat_message.model.dto.request.RagContextChunkDto;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDTO implements Serializable {
    private Long id;
    private String sender;
    private String message;
    private OriginType originType;
    private List<RagContextChunkDto> contextChunks;
}
