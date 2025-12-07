package com.app.ragchatapp.chat_message.model.dto.request;

import com.app.ragchatapp.chat_message.model.dto.enums.ChunkSource;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class RagContextChunkDto {
    private ChunkSource source;
    private String documentId;
    private String chunkId;
    private double score;
    private String text;
    private String url;
}
