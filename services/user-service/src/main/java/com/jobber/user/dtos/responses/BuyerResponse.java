package com.jobber.user.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BuyerResponse {

  private String id;
  private String username;
  private String email;
  private String profilePicture;
  private String country;
  private Boolean isSeller;
  private List<String> purchasedGigs;
  private LocalDateTime createdAt;
}
