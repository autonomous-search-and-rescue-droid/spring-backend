package com.cosmicbook.search_and_rescue_droid.repository;

import com.cosmicbook.search_and_rescue_droid.model.ERole;
import com.cosmicbook.search_and_rescue_droid.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(ERole name);

}
