package com.app.ragchatapp.user.controller;

import com.app.ragchatapp.user.model.dto.request.UserCreateRequestDTO;
import com.app.ragchatapp.user.model.dto.response.UserCreateResponseDTO;
import com.app.ragchatapp.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    private ResponseEntity<UserCreateResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.createUser(requestDTO));
    }
}
