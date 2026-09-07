package com.tejas.incidentplatform.controller;

import com.tejas.incidentplatform.dto.AuthenticationResponse;
import com.tejas.incidentplatform.dto.LoginRequest;
import com.tejas.incidentplatform.dto.RegisterRequest;
import com.tejas.incidentplatform.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login( @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request)
        );

    }
}