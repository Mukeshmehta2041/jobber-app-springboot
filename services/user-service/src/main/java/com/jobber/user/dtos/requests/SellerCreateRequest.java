package com.jobber.user.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

/**
 * DTO for creating a new Seller profile.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerCreateRequest {

  @NotBlank(message = "Full name is required")
  private String fullName;

  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Profile picture URL is required")
  private String profilePicture;

  @NotBlank(message = "Description is required")
  private String description;

  @NotBlank(message = "Profile Public ID is required")
  private String profilePublicId;

  private String oneliner;

  @NotBlank(message = "Country is required")
  private String country;

  @NotEmpty(message = "Languages list cannot be empty")
  private List<LanguageRequest> languages;

  @NotEmpty(message = "Skills list cannot be empty")
  private List<String> skills;

  private List<ExperienceRequest> experience;

  private List<EducationRequest> education;

  private List<String> socialLinks;

  private List<CertificateRequest> certificates;

  // =================== Nested DTOs =================== //

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class LanguageRequest {
    @NotBlank(message = "Language name is required")
    private String language;

    @NotBlank(message = "Language level is required")
    private String level;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ExperienceRequest {
    private String company;
    private String title;
    private String startDate;
    private String endDate;
    private String description;
    private boolean currentlyWorkingHere;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EducationRequest {
    private String country;
    private String university;
    private String title;
    private String major;
    private String year;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CertificateRequest {
    private String name;
    private String from;
    private Integer year;
  }
}
