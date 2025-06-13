package com.jobber.user.services.seller;

import com.jobber.user.dtos.requests.SellerCreateRequest;
import com.jobber.user.models.Seller;

public interface SellerService {
    Seller create(Seller request);
}
