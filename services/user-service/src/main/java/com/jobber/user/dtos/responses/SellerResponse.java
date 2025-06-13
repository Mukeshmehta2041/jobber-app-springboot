package com.jobber.user.dtos.responses;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO representing a Seller profile to be sent to clients.
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
    private String description;
    private String profilePublicId;
    private String oneliner;
    private String country;

    private List<Language> languages;
    private List<String> skills;
    private int ratingsCount;
    private double averageRating;
    private RatingCategories ratingCategories;
    private int responseTime;
    private LocalDate recentDelivery;

    private List<Experience> experience;
    private List<Education> education;
    private List<String> socialLinks;
    private List<Certificate> certificates;

    private int ongoingJobs;
    private int completedJobs;
    private int cancelledJobs;
    private double totalEarnings;
    private int totalGigs;
    private LocalDateTime createdAt;

    // Nested DTOs

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Language {
        private String language;
        private String level;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Experience {
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
    public static class Education {
        private String country;
        private String university;
        private String title;
        private String major;
        private String year;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Certificate {
        private String name;
        private String from;
        private Integer year;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RatingCategories {
        private RatingLevel five = new RatingLevel();
        private RatingLevel four = new RatingLevel();
        private RatingLevel three = new RatingLevel();
        private RatingLevel two = new RatingLevel();
        private RatingLevel one = new RatingLevel();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RatingLevel {
        private int value;
        private int count;
    }
}
