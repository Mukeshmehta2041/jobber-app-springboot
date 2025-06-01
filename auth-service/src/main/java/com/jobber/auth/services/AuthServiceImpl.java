package com.jobber.auth.services;

import com.jobber.auth.domain.dtos.request.LoginRequest;
import com.jobber.auth.domain.dtos.request.SignUpRequest;
import com.jobber.auth.domain.dtos.response.AuthResponse;
import com.jobber.auth.entities.Auth;
import com.jobber.auth.exceptions.ResourceAlreadyExistException;
import com.jobber.auth.exceptions.ResourceNotFoundException;
import com.jobber.auth.repositories.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;

    @Override
    public AuthResponse register(SignUpRequest request) {
        boolean userExists = authRepository.findByEmailOrUsername(
                request.getEmail(), request.getUsername()
        ).isPresent();

        if (userExists) {
            throw new ResourceAlreadyExistException("User with this email or username already exists");
        }

        Auth newAuth = Auth.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .country(request.getCountry())
                .profilePicture(request.getProfilePicture())
                .browserName(request.getBrowserName())
                .deviceType(request.getDeviceType())
                .build();

        authRepository.save(newAuth);

        // TODO: generate verification link
        // TODO: publish event
        // TODO: generate JWT or session token

        String token = jwtService.generateToken(newAuth.getUsername());

        return AuthResponse.builder()
                .userId(newAuth.getId())
                .token(token)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Auth auth = authRepository.findByEmailOrUsername(
                loginRequest.getEmail(), loginRequest.getUsername()
        ).orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), auth.getPassword())) {
            throw new ResourceNotFoundException("Invalid credentials");
        }

        // If login from a new browser, trigger OTP (just a placeholder)
        boolean isSameBrowser = loginRequest.getBrowserName() != null &&
                loginRequest.getBrowserName().equals(auth.getBrowserName());

        if (!isSameBrowser) {
            // TODO: Send OTP, email notification, or security alert
        }

        String token = jwtService.generateToken(auth.getUsername());

        // TODO: generate JWT or session token
        return AuthResponse.builder()
                .userId(auth.getId())
                .token(token)
                .build();
    }
}
