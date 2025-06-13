package com.jobber.auth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceIntegerationTest {

    @InjectMocks
    private JwtService jwtService;

    private String testUsername;
    private String validToken;
    private String expiredToken;

    @BeforeEach
    void setUp() {
        testUsername = "testuser@example.com";
        validToken = jwtService.generateToken(testUsername);

        // Generate an expired token (1 hour in the past)
        long oneHourAgo = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1);
        expiredToken = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date(oneHourAgo))
                .setExpiration(new Date(oneHourAgo + TimeUnit.MINUTES.toMillis(1))) // Expired 59 minutes ago
                .signWith(jwtService.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    void generateToken_ValidUsername_ReturnsToken() {
        // Act
        String token = jwtService.generateToken(testUsername);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ValidToken_ReturnsCorrectUsername() {
        // Act
        String extractedUsername = jwtService.extractUsername(validToken);

        // Assert
        assertEquals(testUsername, extractedUsername);
    }


    @Test
    void extractUsername_InvalidToken_ThrowsException() {
        // Arrange
        String invalidToken = "invalid.token.string";

        // Act & Assert
        assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken));
    }

    @Test
    void extractExpiration_ValidToken_ReturnsFutureDate() {
        // Act
        Date expiration = jwtService.extractExpiration(validToken);

        // Assert
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void isTokenExpired_ValidToken_ReturnsFalse() {
        // Act
        boolean isExpired = jwtService.isTokenExpired(validToken);

        // Assert
        assertFalse(isExpired);
    }

//    @Test
//    void isTokenExpired_ExpiredToken_ReturnsTrue() {
//        // Act
//        boolean isExpired = jwtService.isTokenExpired(expiredToken);
//
//        // Assert
//        assertTrue(isExpired);
//    }

    @Test
    void isTokenValid_ValidTokenAndUsername_ReturnsTrue() {
        // Act
        boolean isValid = jwtService.isTokenValid(validToken, testUsername);

        // Assert
        assertTrue(isValid);
    }

//    @Test
//    void isTokenValid_ExpiredToken_ReturnsFalse() {
//        // Act
//        boolean isValid = jwtService.isTokenValid(expiredToken, testUsername);
//
//        // Assert
//        assertFalse(isValid);
//    }

    @Test
    void isTokenValid_WrongUsername_ReturnsFalse() {
        // Act
        boolean isValid = jwtService.isTokenValid(validToken, "wronguser@example.com");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void isTokenValid_TamperedToken_ThrowsException() {
        // Arrange
        String tamperedToken = validToken.substring(0, validToken.length() - 5) + "abcde";

        // Act & Assert
        assertThrows(SignatureException.class,
                () -> jwtService.isTokenValid(tamperedToken, testUsername));
    }

    @Test
    void extractAllClaims_ValidToken_ReturnsClaims() {
        // Act
        Claims claims = jwtService.extractAllClaims(validToken);

        // Assert
        assertNotNull(claims);
        assertEquals(testUsername, claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void extractAllClaims_ExpiredToken_ThrowsException() {
        // Act & Assert
        assertThrows(ExpiredJwtException.class,
                () -> jwtService.extractAllClaims(expiredToken));
    }

}
