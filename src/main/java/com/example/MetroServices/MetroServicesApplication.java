package com.example.MetroServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching  // Enables caching
public class MetroServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetroServicesApplication.class, args);
	}

}
