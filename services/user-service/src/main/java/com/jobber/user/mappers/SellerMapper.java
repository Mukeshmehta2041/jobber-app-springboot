package com.jobber.user.mappers;

import com.jobber.user.dtos.requests.SellerCreateRequest;
import com.jobber.user.dtos.responses.SellerResponse;
import com.jobber.user.models.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SellerMapper {

  SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);

  // ========== Entity → Response ==========

  SellerResponse toSellerResponse(Seller seller);

  SellerResponse.RatingCategoriesResponse toRatingCategoriesResponse(Seller.RatingCategories ratingCategories);

  SellerResponse.RatingLevelResponse toRatingLevelResponse(Seller.RatingLevel ratingLevel);

  SellerResponse.LanguageResponse toLanguageResponse(Seller.Language language);

  List<SellerResponse.LanguageResponse> toLanguageResponseList(List<Seller.Language> languages);

  SellerResponse.ExperienceResponse toExperienceResponse(Seller.Experience experience);

  List<SellerResponse.ExperienceResponse> toExperienceResponseList(List<Seller.Experience> experiences);

  SellerResponse.EducationResponse toEducationResponse(Seller.Education education);

  List<SellerResponse.EducationResponse> toEducationResponseList(List<Seller.Education> educationList);

  SellerResponse.CertificateResponse toCertificateResponse(Seller.Certificate certificate);

  List<SellerResponse.CertificateResponse> toCertificateResponseList(List<Seller.Certificate> certificateList);

  // ========== CreateRequest → Entity ==========

  Seller toSeller(SellerCreateRequest request);

  Seller.Language toLanguage(SellerCreateRequest.LanguageRequest request);

  List<Seller.Language> toLanguageList(List<SellerCreateRequest.LanguageRequest> requestList);

  Seller.Experience toExperience(SellerCreateRequest.ExperienceRequest request);

  List<Seller.Experience> toExperienceList(List<SellerCreateRequest.ExperienceRequest> requestList);

  Seller.Education toEducation(SellerCreateRequest.EducationRequest request);

  List<Seller.Education> toEducationList(List<SellerCreateRequest.EducationRequest> requestList);

  Seller.Certificate toCertificate(SellerCreateRequest.CertificateRequest request);

  List<Seller.Certificate> toCertificateList(List<SellerCreateRequest.CertificateRequest> requestList);
}
