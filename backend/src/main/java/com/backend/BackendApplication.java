package com.backend;

import com.backend.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {
	private final UserService userService;

	@Autowired
	public BackendApplication(final UserService userService){
		this.userService =userService;
	}

	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			try {
				userService.registerUser("manager", "heslo", "Dominik", "Prokop", "email@gmail.com", "+429787787777", "MANAGER");
				userService.registerUser("admin", "heslo", "Dominik", "Prokop", "email@gmail.com", "+429787787777", "ADMIN");
				userService.registerUser("user", "heslo", "Dominik", "Prokop", "email@gmail.com", "+429787787777", "TENANT");
			} catch (final IllegalArgumentException exception) {
				System.out.println("Error during user registration: " + exception.getMessage());
			}
		};
	}

	public static void main(final String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
