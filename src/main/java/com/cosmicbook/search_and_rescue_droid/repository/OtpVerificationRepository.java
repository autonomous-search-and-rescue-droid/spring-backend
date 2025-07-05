package com.cosmicbook.search_and_rescue_droid.repository;

import com.cosmicbook.search_and_rescue_droid.model.OTPVerification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OtpVerificationRepository extends MongoRepository<OTPVerification,String> {
    Optional<OTPVerification> findByEmail(String email);
}
