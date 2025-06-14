package com.jobber.user.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BuyerCreateRequest {

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  private String profilePicture;

  @NotBlank(message = "Country is required")
  private String country;

  @NotNull(message = "isSeller field must be specified")
  private Boolean isSeller;
}
