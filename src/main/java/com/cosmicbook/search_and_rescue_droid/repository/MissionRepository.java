package com.cosmicbook.search_and_rescue_droid.repository;

import com.cosmicbook.search_and_rescue_droid.model.Missions;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MissionRepository extends MongoRepository<Missions,String> {
    Optional<Missions> getById(String id);
    Optional<Missions> getBySpotName(String spotName);
}
