package com.jobber.user.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

/**
 * Request DTO for creating a seller profile.
 * Includes personal details, skills, experience, education, languages, and certificates.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerCreateRequest {

    /** Full name of the seller (required). */
    @NotBlank(message = "Full name is required")
    private String fullName;

    /** Unique username for the seller (required). */
    @NotBlank(message = "Username is required")
    private String username;

    /** Seller's email address (required, must be valid format). */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /** Public URL to seller's profile picture (required). */
    @NotBlank(message = "Profile picture URL is required")
    private String profilePicture;

    /** Public ID for seller's profile (used for CDN storage reference) (required). */
    @NotBlank(message = "Profile public ID is required")
    private String profilePublicId;

    /** Brief description or bio of the seller (required). */
    @NotBlank(message = "Description is required")
    private String description;

    /** One-liner summary about the seller (optional, default is empty). */
    private String oneliner = "";

    /** Country of the seller (required). */
    @NotBlank(message = "Country is required")
    private String country;

    /** List of languages the seller speaks (at least one required). */
    @NotEmpty(message = "Languages list cannot be empty")
    private List<LanguageRequest> languages;

    /** List of skills the seller possesses (at least one required). */
    @NotEmpty(message = "Skills list cannot be empty")
    private List<String> skills;

    /** List of work experiences (optional). */
    private List<ExperienceRequest> experience;

    /** List of educational qualifications (optional). */
    private List<EducationRequest> education;

    /** List of social media or portfolio links (optional). */
    private List<String> socialLinks;

    /** List of certifications the seller has received (optional). */
    private List<CertificateRequest> certificates;

    /**
     * Represents a language known by the seller.
     * Includes language name and proficiency level.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LanguageRequest {

        /** Name of the language (required). */
        @NotBlank(message = "Language name is required")
        private String language;

        /** Proficiency level in the language (required). */
        @NotBlank(message = "Language level is required")
        private String level;
    }

    /**
     * Represents professional experience.
     * Contains job title, company, and period of employment.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExperienceRequest {

        /** Company name (optional, default empty). */
        private String company = "";

        /** Job title or role (optional, default empty). */
        private String title = "";

        /** Start date of employment (format YYYY-MM-DD, optional). */
        private String startDate = "";

        /** End date of employment (format YYYY-MM-DD, optional). */
        private String endDate = "";

        /** Job description (optional). */
        private String description = "";

        /** Indicates if the seller is currently working in this position. */
        private boolean currentlyWorkingHere = false;
    }

    /**
     * Represents an educational qualification.
     * Includes university name, country, major, and year.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EducationRequest {

        /** Country of the university (optional, default empty). */
        private String country = "";

        /** Name of the university (optional, default empty). */
        private String university = "";

        /** Degree or certification title (optional, default empty). */
        private String title = "";

        /** Major or field of study (optional, default empty). */
        private String major = "";

        /** Year of completion (optional, default empty). */
        private String year = "";
    }

    /**
     * Represents a professional certificate or license.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CertificateRequest {

        /** Name of the certificate (required). */
        private String name;

        /** Issuing organization or authority (required). */
        private String from;

        /** Year of certification (optional, can be null). */
        private Integer year;
    }
}
