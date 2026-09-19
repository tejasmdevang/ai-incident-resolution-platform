package com.tejas.incidentplatform.service;

import com.tejas.incidentplatform.dto.RegisterRequest;
import com.tejas.incidentplatform.entity.Role;
import com.tejas.incidentplatform.entity.User;
import com.tejas.incidentplatform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldHashPasswordAndRegisterUser() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Tejas Devang");
        request.setEmail("tejas@example.com");
        request.setPassword("SpringBoot123");

        when(userRepository.existsByEmail("tejas@example.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("SpringBoot123"))
                .thenReturn("hashed-password");

        authService.register(request);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals("tejas@example.com", savedUser.getEmail());
        assertEquals("hashed-password", savedUser.getPasswordHash());
        assertEquals(Role.ENGINEER, savedUser.getRole());
    }
}