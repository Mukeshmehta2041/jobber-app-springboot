package com.jobber.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class that provides security-related beans and
 * configurations.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * Provides a BCryptPasswordEncoder bean for password hashing.
   * BCrypt is a strong password hashing algorithm that automatically handles salt
   * generation.
   *
   * @return BCryptPasswordEncoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures the security filter chain with basic security settings.
   * Currently allows all requests as authentication will be handled by JWT.
   *
   * @param http HttpSecurity instance
   * @return SecurityFilterChain
   * @throws Exception if configuration fails
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // Allow access to Swagger UI and OpenAPI documentation
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
            // Allow access to actuator endpoints
            .requestMatchers("/actuator/**").permitAll()
            // Allow access to H2 console
            .requestMatchers("/h2-console/**").permitAll()
            // All other requests need authentication
            .anyRequest().authenticated())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Enable H2 console
        .headers(headers -> headers.frameOptions().disable());

    return http.build();
  }
}