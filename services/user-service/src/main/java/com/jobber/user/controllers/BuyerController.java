package com.jobber.user.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobber.user.dtos.requests.BuyerCreateRequest;
import com.jobber.user.dtos.responses.BuyerResponse;
import com.jobber.user.services.buyer.BuyerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for handling buyer-related HTTP requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/buyers")
public class BuyerController {

  private final BuyerService buyerService;

  /**
   * Create a new buyer.
   *
   * @param request the request body containing buyer details
   * @return the created buyer response
   */
  @PostMapping
  public ResponseEntity<BuyerResponse> createBuyer(@Valid @RequestBody BuyerCreateRequest request) {
    return ResponseEntity.ok(buyerService.createBuyer(request));
  }

  /**
   * Get a buyer by their ID.
   *
   * @param buyerId the ID of the buyer to retrieve
   * @return the buyer response
   */
  @GetMapping("/id/{buyerId}")
  public ResponseEntity<BuyerResponse> getBuyerById(@PathVariable("buyerId") String buyerId) {
    return ResponseEntity.ok(buyerService.getBuyerById(buyerId));
  }

  /**
   * Get a buyer by their username.
   *
   * @param username the username of the buyer to retrieve
   * @return the buyer response
   */
  @GetMapping("/username/{username}")
  public ResponseEntity<BuyerResponse> getBuyerByUsername(@PathVariable("username") String username) {
    return ResponseEntity.ok(buyerService.getBuyerByUsername(username));
  }

  /**
   * Get a random buyer.
   *
   * @param size the number of buyers to retrieve
   * @return the buyer response
   */
  @GetMapping("/random/{size}")
  public ResponseEntity<List<BuyerResponse>> getRandomBuyer(@PathVariable("size") int size) {
    return ResponseEntity.ok(buyerService.getRandomBuyer(size));
  }

  /**
   * Seed buyers.
   *
   * @param count the number of buyers to seed
   * @return the buyer response
   */
  @PostMapping("/seed/{count}")
  public ResponseEntity<List<BuyerResponse>> seedBuyers(@PathVariable("count") int count) {
    return ResponseEntity.ok(buyerService.seedBuyers(count));
  }
}
