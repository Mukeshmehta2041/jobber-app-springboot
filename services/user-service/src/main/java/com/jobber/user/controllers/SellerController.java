package com.jobber.user.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobber.user.dtos.requests.SellerCreateRequest;
import com.jobber.user.dtos.responses.SellerResponse;
import com.jobber.user.services.SellerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for handling seller-related HTTP requests.
 */
@RestController
@RequestMapping("/api/v1/sellers")
@RequiredArgsConstructor
public class SellerController {

  private final SellerService sellerService;

  /**
   * Create a new seller.
   *
   * @param request the request body containing seller details
   * @return the created seller response
   */
  @PostMapping
  public ResponseEntity<SellerResponse> createSeller(@Valid @RequestBody SellerCreateRequest request) {
    return ResponseEntity.ok(sellerService.createSeller(request));
  }

  /**
   * Get a seller by ID.
   *
   * @param sellerId the seller's ID
   * @return the seller response
   */
  @GetMapping("/id/{sellerId}")
  public ResponseEntity<SellerResponse> getSellerById(@PathVariable("sellerId") String sellerId) {
    return ResponseEntity.ok(sellerService.getSellerById(sellerId));
  }

  /**
   * Get a seller by username.
   *
   * @param username the seller's username
   * @return the seller response
   */
  @GetMapping("/username/{username}")
  public ResponseEntity<SellerResponse> getSellerByUsername(@PathVariable("username") String username) {
    return ResponseEntity.ok(sellerService.getSellerByUsername(username));
  }

  /**
   * Get random seller.
   *
   * @return the random seller response
   */
  @GetMapping("/random/{size}")
  public ResponseEntity<List<SellerResponse>> getRandomSeller(@PathVariable("size") int size) {
    return ResponseEntity.ok(sellerService.getRandomSellers(size));
  }

  /**
   * Seed sellers based on count.
   *
   * @param count the number of sellers to seed
   * @return the seeded sellers
   */
  @PostMapping("/seed/{count}")
  public ResponseEntity<List<SellerResponse>> seedSellers(@PathVariable("count") int count) {
    return ResponseEntity.ok(sellerService.seedSellers(count));
  }
}
