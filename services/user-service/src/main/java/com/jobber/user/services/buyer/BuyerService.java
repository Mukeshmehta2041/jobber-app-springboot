package com.jobber.user.services.buyer;

import java.util.List;

import com.jobber.user.dtos.requests.BuyerCreateRequest;
import com.jobber.user.dtos.responses.BuyerResponse;

public interface BuyerService {

  BuyerResponse createBuyer(BuyerCreateRequest request);

  BuyerResponse getBuyerById(String buyerId);

  BuyerResponse getBuyerByUsername(String username);

  List<BuyerResponse> getRandomBuyer(int size);

  List<BuyerResponse> seedBuyers(int count);
}
