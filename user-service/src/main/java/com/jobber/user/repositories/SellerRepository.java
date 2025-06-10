package com.jobber.user.repositories;

import com.jobber.user.models.Seller;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellerRepository extends MongoRepository<Seller,String> {
    Seller findByEmail(String email);
}
