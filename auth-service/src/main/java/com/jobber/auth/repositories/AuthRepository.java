package com.jobber.auth.repositories;

import com.jobber.auth.entities.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<Auth, UUID> {
    Optional<Auth> findByEmailOrUsername(String email, String username);
}
