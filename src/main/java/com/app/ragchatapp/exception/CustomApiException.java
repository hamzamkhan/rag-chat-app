package com.app.ragchatapp.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomApiException extends RuntimeException {
    public CustomApiException(String message) {
        super(message);
    }
}
