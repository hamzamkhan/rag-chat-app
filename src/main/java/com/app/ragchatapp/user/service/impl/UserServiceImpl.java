package com.app.ragchatapp.user.service.impl;

import com.app.ragchatapp.exception.CustomApiException;
import com.app.ragchatapp.user.model.dto.request.UserCreateRequestDTO;
import com.app.ragchatapp.user.model.dto.response.UserCreateResponseDTO;
import com.app.ragchatapp.user.model.entity.User;
import com.app.ragchatapp.user.model.repo.UserRepository;
import com.app.ragchatapp.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserCreateResponseDTO createUser(UserCreateRequestDTO userCreateRequestDTO) {
        if (userRepository.existsByEmail(userCreateRequestDTO.getEmail())) {
            throw new CustomApiException("User already exists by email: " + userCreateRequestDTO.getEmail());
        }
        User user = User.builder()
                .email(userCreateRequestDTO.getEmail())
                .name(userCreateRequestDTO.getName())
                .build();
        userRepository.save(user);

        return UserCreateResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .id(user.getId())
                .build();
    }
}
