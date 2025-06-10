package com.jobber.user.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a Buyer or Seller in the system.
 * Stored in the "sellers" MongoDB collection.
 */
@Document(collection = "buyers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Buyer {

    /** Unique MongoDB ID */
    @Id
    private String id;

    /** Username of the user */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    /** User's email */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    /** Optional profile picture URL */
    private String profilePicture;

    /** Country of residence */
    @NotBlank(message = "Country is required")
    private String country;

    /** Flag to indicate if user is a seller */
    @NotNull(message = "isSeller field must be specified")
    private Boolean isSeller;

    /** List of purchased gig IDs (stored as ObjectId strings) */
    private List<String> purchasedGigs;

    /** Account creation timestamp */
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
