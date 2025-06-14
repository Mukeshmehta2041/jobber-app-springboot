package com.jobber.user.mappers;

import org.mapstruct.Mapper;

import com.jobber.user.dtos.requests.BuyerCreateRequest;
import com.jobber.user.dtos.responses.BuyerResponse;
import com.jobber.user.models.Buyer;

@Mapper(componentModel = "spring")
public interface BuyerMapper {

  Buyer toBuyer(BuyerCreateRequest request);

  BuyerResponse toBuyerResponse(Buyer buyer);
}