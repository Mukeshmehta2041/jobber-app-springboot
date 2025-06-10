package com.jobber.user.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB document representing a Seller profile.
 * Includes metadata such as personal info, ratings, experience, education, and job statistics.
 */
@Document(collection = "sellers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

    /** Unique identifier for the seller (MongoDB ObjectId) */
    @Id
    private String id;

    /** Full name of the seller */
    @NotBlank(message = "Full name is required")
    private String fullName;

    /** Unique username of the seller */
    @NotBlank(message = "Username is required")
    private String username;

    /** Unique and valid email address */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /** Profile picture URL (e.g., Cloudinary) */
    @NotBlank(message = "Profile picture URL is required")
    private String profilePicture;

    /** Detailed description of the seller */
    @NotBlank(message = "Description is required")
    private String description;

    /** Public ID for the profile picture (e.g., Cloudinary publicId) */
    @NotBlank(message = "Profile Public ID is required")
    private String profilePublicId;

    /** One-liner tagline shown on seller's profile */
    @Builder.Default
    private String oneliner = "";

    /** Country of the seller */
    @NotBlank(message = "Country is required")
    private String country;

    /** List of languages the seller speaks */
    @NotEmpty(message = "Languages list cannot be empty")
    private List<Language> languages;

    /** List of skills offered by the seller */
    @NotEmpty(message = "Skills list cannot be empty")
    private List<String> skills;

    /** Total number of ratings received */
    @Builder.Default
    private int ratingsCount = 0;

    /** Sum of all rating values for average calculation */
    @Builder.Default
    private int ratingSum = 0;

    /** Ratings broken into categories (1 to 5 stars) */
    @Builder.Default
    private RatingCategories ratingCategories = new RatingCategories();

    /** Average response time in hours */
    @Builder.Default
    private int responseTime = 0;

    /** Date of most recent delivery */
    private LocalDate recentDelivery;

    /** Professional work experiences */
    private List<Experience> experience;

    /** Academic history */
    private List<Education> education;

    /** List of external social links */
    private List<String> socialLinks;

    /** List of professional certifications */
    private List<Certificate> certificates;

    /** Number of ongoing jobs */
    @Builder.Default
    private int ongoingJobs = 0;

    /** Number of completed jobs */
    @Builder.Default
    private int completedJobs = 0;

    /** Number of cancelled jobs */
    @Builder.Default
    private int cancelledJobs = 0;

    /** Total earnings by the seller */
    @Builder.Default
    private double totalEarnings = 0;

    /** Total number of gigs created by the seller */
    @Builder.Default
    private int totalGigs = 0;

    /** Timestamp of seller account creation */
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // =================== Embedded/Nested Documents =================== //

    /**
     * Represents a language the seller speaks.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Language {
        @NotBlank(message = "Language name is required")
        private String language;

        @NotBlank(message = "Language level is required")
        private String level;  // e.g., Fluent, Native
    }

    /**
     * Represents a single job experience entry.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Experience {
        private String company = "";
        private String title = "";
        private String startDate = "";
        private String endDate = "";
        private String description = "";
        private boolean currentlyWorkingHere = false;
    }

    /**
     * Represents an academic qualification.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Education {
        private String country = "";
        private String university = "";
        private String title = "";
        private String major = "";
        private String year = "";
    }

    /**
     * Represents a professional certificate.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Certificate {
        private String name;
        private String from;
        private Integer year;
    }

    /**
     * Represents all rating levels from 1 to 5.
     */
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

    /**
     * Represents a single star rating level with value and count.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RatingLevel {
        private int value = 0;
        private int count = 0;
    }
}
