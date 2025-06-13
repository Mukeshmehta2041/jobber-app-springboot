package com.jobber.auth.controllers;

import com.jobber.auth.domain.dtos.request.LoginRequest;
import com.jobber.auth.domain.dtos.request.SignUpRequest;
import com.jobber.auth.domain.dtos.response.AuthResponse;
import com.jobber.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signUp(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authService.register(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest)); 
    }

    @GetMapping("/user-info/{username}")
    public ResponseEntity<AuthResponse> getUserInfo(@PathVariable String username){
        return ResponseEntity.ok(authService.getByUserName(username));
    }
}
