package com.jobber.user.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobber.user.models.Buyer;

public interface BuyerRepository extends MongoRepository<Buyer, String> {

  Buyer findByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
