package com.jobber.user.services.seller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobber.common.exceptions.ConflictException;
import com.jobber.common.exceptions.NotFoundException;
import com.jobber.user.dtos.requests.SellerCreateRequest;
import com.jobber.user.dtos.responses.SellerResponse;
import com.jobber.user.mappers.SellerMapper;
import com.jobber.user.models.Seller;
import com.jobber.user.repositories.SellerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the SellerService interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

  private final SellerRepository sellerRepository;
  private final SellerMapper sellerMapper;
  private final Random random = new Random();

  /**
   * {@inheritDoc}
   */
  @Override
  public SellerResponse createSeller(SellerCreateRequest request) {
    log.info("Creating seller: {}", request);

    if (sellerRepository.existsByEmail(request.getEmail())) {
      throw new ConflictException("Seller already exists");
    }

    Seller newSeller = sellerMapper.toSeller(request);
    Seller createdSeller = sellerRepository.save(newSeller);

    log.info("Created seller: {}", createdSeller);

    return sellerMapper.toSellerResponse(createdSeller);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SellerResponse getSellerById(String sellerId) {
    log.info("Getting seller by id: {}", sellerId);
    Seller seller = sellerRepository.findById(sellerId)
        .orElseThrow(() -> new NotFoundException("Seller not found"));
    return sellerMapper.toSellerResponse(seller);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SellerResponse getSellerByUsername(String username) {
    log.info("Getting seller by username: {}", username);
    Seller seller = sellerRepository.findByUsername(username)
        .orElseThrow(() -> new NotFoundException("Seller not found"));
    return sellerMapper.toSellerResponse(seller);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SellerResponse> getRandomSellers(int count) {
    log.info("Getting {} random sellers", count);
    List<Seller> allSellers = sellerRepository.findAll();

    if (allSellers.isEmpty()) {
      throw new NotFoundException("No sellers available");
    }

    // Ensure we don't request more sellers than available
    int maxCount = Math.min(count, allSellers.size());

    // Shuffle and pick the first 'maxCount' sellers
    return random.ints(0, allSellers.size())
        .distinct()
        .limit(maxCount)
        .mapToObj(allSellers::get)
        .map(sellerMapper::toSellerResponse)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SellerResponse> seedSellers(int count) {
    log.info("Seeding {} sellers", count);

    List<Seller> sellers = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      Seller seller = Seller.builder()
          .fullName("Seller " + i)
          .username("seller" + i)
          .email("seller" + i + "@example.com")
          .profilePicture("https://example.com/images/seller" + i + ".jpg")
          .profilePublicId("publicId" + i)
          .description("Experienced seller in various domains.")
          .oneliner("Expert in multiple fields")
          .country("India")
          .languages(List.of(new Seller.Language("English", "Fluent"), new Seller.Language("Hindi", "Native")))
          .skills(List.of("Java", "Spring Boot", "MongoDB"))
          .recentDelivery(LocalDate.now().minusDays(i))
          .experience(List.of(
              new Seller.Experience("Company " + i, "Developer", "2020", "2022", "Worked on backend systems", false)))
          .education(
              List.of(new Seller.Education("India", "University " + i, "Bachelor's", "Computer Science", "2020")))
          .socialLinks(List.of("https://linkedin.com/in/seller" + i))
          .certificates(List.of(new Seller.Certificate("Java Certification", "Oracle", 2021)))
          .ratingsCount(0)
          .ratingSum(0)
          .ongoingJobs(0)
          .completedJobs(0)
          .cancelledJobs(0)
          .totalEarnings(0)
          .totalGigs(0)
          .responseTime(0)
          .createdAt(LocalDateTime.now().minusDays(i))
          .build();

      sellers.add(seller);
    }

    // Save all sellers to the DB
    sellerRepository.saveAll(sellers);

    // Map to response DTOs
    return sellers.stream()
        .map(sellerMapper::toSellerResponse)
        .collect(Collectors.toList());
  }

}
