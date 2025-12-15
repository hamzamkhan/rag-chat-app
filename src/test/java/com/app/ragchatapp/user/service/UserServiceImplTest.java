package com.app.ragchatapp.user.service;

import com.app.ragchatapp.exception.CustomApiException;
import com.app.ragchatapp.user.model.dto.request.UserCreateRequestDTO;
import com.app.ragchatapp.user.model.dto.response.UserCreateResponseDTO;
import com.app.ragchatapp.user.model.entity.User;
import com.app.ragchatapp.user.model.repo.UserRepository;
import com.app.ragchatapp.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_throwsWhenEmailAlreadyExists() {
        UserCreateRequestDTO request = new UserCreateRequestDTO();
        // Using reflection to set fields because DTO has no setters
        setField(request, "name", "Test User");
        setField(request, "email", "test@example.com");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(CustomApiException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_savesAndReturnsResponse() {
        UserCreateRequestDTO request = new UserCreateRequestDTO();
        setField(request, "name", "Test User");
        setField(request, "email", "test@example.com");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        User saved = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserCreateResponseDTO response = userService.createUser(request);

        assertNotNull(response.getId());
        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
