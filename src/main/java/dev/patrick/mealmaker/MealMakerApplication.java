package dev.patrick.mealmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MealMakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealMakerApplication.class, args);
	}

}
