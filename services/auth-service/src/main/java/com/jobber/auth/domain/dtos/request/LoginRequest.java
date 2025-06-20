package com.jobber.auth.domain.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    private String browserName;
    private String deviceType;
}
