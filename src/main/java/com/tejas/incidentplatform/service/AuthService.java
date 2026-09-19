package com.tejas.incidentplatform.service;

import com.tejas.incidentplatform.dto.AuthenticationResponse;
import com.tejas.incidentplatform.dto.LoginRequest;
import com.tejas.incidentplatform.dto.RegisterRequest;
import com.tejas.incidentplatform.entity.Role;
import com.tejas.incidentplatform.entity.User;
import com.tejas.incidentplatform.exception.EmailAlreadyExistsException;
import com.tejas.incidentplatform.repository.UserRepository;
import com.tejas.incidentplatform.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import java.time.OffsetDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequest request) {

    String normalizedEmail =
            request.getEmail().trim().toLowerCase();

    if (userRepository.existsByEmail(normalizedEmail)) {
        throw new EmailAlreadyExistsException(normalizedEmail);
    }

    User user = new User();
    user.setName(request.getName().trim());
    user.setEmail(normalizedEmail);
    user.setPasswordHash(
            passwordEncoder.encode(request.getPassword())
    );
    user.setRole(Role.ENGINEER);
    user.setCreatedAt(OffsetDateTime.now());

    userRepository.save(user);
}

@Transactional(readOnly = true)
public AuthenticationResponse login(LoginRequest request) {


    String normalizedEmail =
            request.getEmail().trim().toLowerCase();

            Authentication authentication =
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            normalizedEmail,
                            request.getPassword()
                    )
            );

            String token =
            jwtService.generateToken(authentication);

    return new AuthenticationResponse(token);
}
}