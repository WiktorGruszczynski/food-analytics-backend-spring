package org.example.foodbudgetbackendspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FoodbudgetBackendSpringApplication {
	public static void main(String[] args) {
		SpringApplication.run(FoodbudgetBackendSpringApplication.class, args);
	}
}
