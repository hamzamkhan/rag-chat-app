package com.app.ragchatapp.user.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateResponseDTO implements Serializable {
    private UUID id;
    private String name;
    private String email;
}
