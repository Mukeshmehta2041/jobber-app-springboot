package com.jobber.user.repositories;

import com.jobber.user.models.Seller;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellerRepository extends MongoRepository<Seller, String> {
  Seller findByEmail(String email);

  boolean existsByEmail(String email);

  Optional<Seller> findByUsername(String username);
}
