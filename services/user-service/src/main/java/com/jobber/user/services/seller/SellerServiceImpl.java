package com.jobber.user.services.seller;

import com.jobber.user.dtos.requests.SellerCreateRequest;
import com.jobber.user.models.Seller;
import com.jobber.user.repositories.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of SellerService.
 * Handles business logic related to seller creation and management.
 */
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    /**
     * Creates a new seller in the system.
     *
     * @param request the Seller entity to be saved
     * @return the created Seller
     * @throws RuntimeException if a seller with the same email already exists
     */
    @Override
    public Seller create(Seller request) {
        Seller sellerExists = sellerRepository.findByEmail(request.getEmail());
        if (sellerExists != null) {
            throw new RuntimeException("Seller already exists with email: " + request.getEmail());
        }

        return sellerRepository.save(request);
    }
}
