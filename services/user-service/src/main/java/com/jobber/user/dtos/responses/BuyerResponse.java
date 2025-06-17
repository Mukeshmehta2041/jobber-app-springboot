package com.jobber.user.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
