package com.cosmicbook.search_and_rescue_droid.repository;

import com.cosmicbook.search_and_rescue_droid.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken,String> {

    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(String userId);

}
