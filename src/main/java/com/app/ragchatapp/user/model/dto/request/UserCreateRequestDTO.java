package com.app.ragchatapp.user.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class UserCreateRequestDTO implements Serializable {
    private String name;
    private String email;
}
