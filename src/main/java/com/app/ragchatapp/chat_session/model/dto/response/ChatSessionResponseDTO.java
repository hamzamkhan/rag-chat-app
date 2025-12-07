package com.app.ragchatapp.chat_session.model.dto.response;


import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionResponseDTO implements Serializable {
    private UUID id;
    private String userId;
    private String title;
    private boolean favorite;
}
