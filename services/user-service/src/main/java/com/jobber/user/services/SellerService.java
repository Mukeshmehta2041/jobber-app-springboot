package com.jobber.user.services;

import java.util.List;

import com.jobber.user.dtos.requests.SellerCreateRequest;
import com.jobber.user.dtos.responses.SellerResponse;

public interface SellerService {

  SellerResponse createSeller(SellerCreateRequest request);

  SellerResponse getSellerById(String sellerId);

  SellerResponse getSellerByUsername(String username);

  List<SellerResponse> getRandomSellers(int count);

  List<SellerResponse> seedSellers(int count);
}
