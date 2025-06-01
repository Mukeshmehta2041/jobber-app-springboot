package com.jobber.auth.services;

import com.jobber.auth.domain.dtos.request.LoginRequest;
import com.jobber.auth.domain.dtos.request.SignUpRequest;
import com.jobber.auth.domain.dtos.response.AuthResponse;

public interface AuthService {
    AuthResponse register(SignUpRequest signUpRequest);
    AuthResponse login(LoginRequest loginRequest);
}
