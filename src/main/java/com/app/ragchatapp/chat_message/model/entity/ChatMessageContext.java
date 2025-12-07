package com.app.ragchatapp.chat_message.model.entity;

import com.app.ragchatapp.chat_message.model.dto.enums.ChunkSource;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chat_message_context")
@Getter
@Setter
public class ChatMessageContext {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id")
    private ChatMessage message;

    @Column(nullable = false)
    private ChunkSource source; // e.g. "VECTOR_DB", "SEARCH_INDEX", "KB"

    @Column(nullable = false)
    private String documentId;  // ID in your vector / docs DB

    @Column(nullable = false)
    private String chunkId;     // passage/chunk identifier

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private double score;      // similarity or ranking score

    @Column(columnDefinition = "TEXT")
    private String text;
}
