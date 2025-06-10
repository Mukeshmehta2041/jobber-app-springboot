package com.jobber.user.controllers;


import com.jobber.user.dtos.requests.SellerCreateRequest;
import com.jobber.user.dtos.responses.SellerResponse;
import com.jobber.user.mappers.SellerMapper;
import com.jobber.user.models.Seller;
import com.jobber.user.services.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final SellerMapper sellerMapper = Mappers.getMapper(SellerMapper.class);

    public ResponseEntity<SellerResponse> createSeller(SellerCreateRequest request){
        Seller sellerData = sellerMapper.toModel(request);
        Seller createdSeller = sellerService.create(sellerData);
        SellerResponse sellerResponse = sellerMapper.toResponse(createdSeller);
        return ResponseEntity.ok(sellerResponse);
    }
}
