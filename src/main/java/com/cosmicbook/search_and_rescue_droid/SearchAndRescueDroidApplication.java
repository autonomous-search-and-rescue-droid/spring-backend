package com.cosmicbook.search_and_rescue_droid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class SearchAndRescueDroidApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchAndRescueDroidApplication.class, args);
	}

}
