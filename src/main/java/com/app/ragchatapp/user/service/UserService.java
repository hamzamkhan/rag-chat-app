package com.app.ragchatapp.user.service;


import com.app.ragchatapp.exception.CustomApiException;
import com.app.ragchatapp.user.model.dto.request.UserCreateRequestDTO;
import com.app.ragchatapp.user.model.dto.response.UserCreateResponseDTO;

public interface UserService {
    UserCreateResponseDTO createUser(UserCreateRequestDTO userCreateRequestDTO) throws CustomApiException;
}
