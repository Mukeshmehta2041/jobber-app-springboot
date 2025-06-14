package com.jobber.user.services.buyer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobber.common.exceptions.ConflictException;
import com.jobber.common.exceptions.NotFoundException;
import com.jobber.user.repositories.BuyerRepository;

import com.jobber.user.dtos.requests.BuyerCreateRequest;
import com.jobber.user.dtos.responses.BuyerResponse;
import com.jobber.user.mappers.BuyerMapper;
import com.jobber.user.models.Buyer;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of the BuyerService interface.
 */
@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

  private final BuyerRepository buyerRepository;
  private final BuyerMapper buyerMapper;

  /**
   * {@inheritDoc}
   */
  @Override
  public BuyerResponse createBuyer(BuyerCreateRequest request) {
    if (buyerRepository.existsByUsername(request.getUsername())) {
      throw new ConflictException("Buyer already exists");
    }

    if (buyerRepository.existsByEmail(request.getEmail())) {
      throw new ConflictException("Buyer already exists");
    }

    return buyerMapper.toBuyerResponse(buyerRepository.save(buyerMapper.toBuyer(request)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BuyerResponse getBuyerById(String buyerId) {
    return buyerMapper.toBuyerResponse(buyerRepository.findById(buyerId)
        .orElseThrow(() -> new NotFoundException("Buyer not found")));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BuyerResponse getBuyerByUsername(String username) {
    return buyerMapper.toBuyerResponse(buyerRepository.findByUsername(username));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BuyerResponse> getRandomBuyer(int size) {
    return buyerRepository.findAll().stream().map(buyerMapper::toBuyerResponse).collect(Collectors.toList());
  }

  /**
   * Seeds the database with a specified number of dummy Buyer entries.
   *
   * @param count the number of buyers to seed
   * @return list of BuyerResponse DTOs for the seeded buyers
   */
  @Override
  public List<BuyerResponse> seedBuyers(int count) {
    List<Buyer> buyers = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      BuyerCreateRequest request = new BuyerCreateRequest();
      request.setUsername("buyer_" + i);
      request.setEmail("buyer" + i + "@example.com");
      request.setProfilePicture("https://example.com/images/buyer" + i + ".jpg");
      request.setCountry("India");
      request.setIsSeller(false);

      buyers.add(buyerMapper.toBuyer(request));
    }

    return buyerRepository.saveAll(buyers)
        .stream()
        .map(buyerMapper::toBuyerResponse)
        .toList();
  }

}
