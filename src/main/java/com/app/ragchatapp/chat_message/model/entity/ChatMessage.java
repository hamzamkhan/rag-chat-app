package com.app.ragchatapp.chat_message.model.entity;

import com.app.ragchatapp.chat_session.model.entity.ChatSession;
import com.app.ragchatapp.chat_message.model.dto.enums.OriginType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_message")
@Getter
@Setter
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id")
    private ChatSession chatSession;

    @Column(nullable = false)
    private OriginType originType;

    @Column
    private Instant createdAt;

    @Column
    private String message;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageContext> context = new ArrayList<>();

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }

    public void addContextChunk(ChatMessageContext ctx) {
        context.add(ctx);
        ctx.setMessage(this);
    }

    public void clearContextChunks() {
        context.forEach(c -> c.setMessage(null));
        context.clear();
    }
}
