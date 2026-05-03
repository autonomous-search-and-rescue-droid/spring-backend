package com.cosmicbook.search_and_rescue_droid.config;

import com.cosmicbook.search_and_rescue_droid.model.ERole;
import com.cosmicbook.search_and_rescue_droid.model.Role;
import com.cosmicbook.search_and_rescue_droid.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RoleSeeder.class);

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        seedRole(ERole.ROLE_ADMIN);
        seedRole(ERole.ROLE_USER);
        seedRole(ERole.ROLE_COMMON);
    }

    private void seedRole(ERole roleName) {
        roleRepository.findByName(roleName).orElseGet(() -> {
            logger.info("Creating missing role {}", roleName);
            return roleRepository.save(new Role(roleName));
        });
    }
}
