package com.jobber.user.mappers;

import com.jobber.user.dtos.requests.SellerCreateRequest;
import com.jobber.user.dtos.responses.SellerResponse;
import com.jobber.user.models.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);

    SellerResponse toResponse(Seller seller);
    Seller toModel(SellerCreateRequest request);
}
