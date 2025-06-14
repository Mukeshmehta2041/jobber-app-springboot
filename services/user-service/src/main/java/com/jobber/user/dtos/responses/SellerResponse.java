package com.jobber.user.dtos.responses;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for sending Seller data to the client.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponse {

  private String id;

  private String fullName;

  private String username;

  private String email;

  private String profilePicture;

  private String profilePublicId;

  private String description;

  private String oneliner;

  private String country;

  private List<LanguageResponse> languages;

  private List<String> skills;

  private int ratingsCount;

  private int ratingSum;

  private RatingCategoriesResponse ratingCategories;

  private int responseTime;

  private LocalDate recentDelivery;

  private List<ExperienceResponse> experience;

  private List<EducationResponse> education;

  private List<String> socialLinks;

  private List<CertificateResponse> certificates;

  private int ongoingJobs;

  private int completedJobs;

  private int cancelledJobs;

  private double totalEarnings;

  private int totalGigs;

  private LocalDateTime createdAt;

  // =================== Nested Response DTOs =================== //

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class LanguageResponse {
    private String language;
    private String level;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ExperienceResponse {
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
  public static class EducationResponse {
    private String country;
    private String university;
    private String title;
    private String major;
    private String year;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CertificateResponse {
    private String name;
    private String from;
    private Integer year;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RatingCategoriesResponse {
    private RatingLevelResponse five;
    private RatingLevelResponse four;
    private RatingLevelResponse three;
    private RatingLevelResponse two;
    private RatingLevelResponse one;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RatingLevelResponse {
    private int value;
    private int count;
  }
}
