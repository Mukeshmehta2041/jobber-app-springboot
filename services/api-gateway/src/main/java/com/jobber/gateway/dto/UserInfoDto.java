package com.jobber.gateway.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
public class UserInfoDto {
    private UUID userId;
    private String username;
    private String email;
}
