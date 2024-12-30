package com.backend;

import com.backend.service.interfaces.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {
	private final @NotNull UserService userService;

	@Autowired
	public BackendApplication(final @NotNull UserService userService){
		this.userService =userService;
	}

	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			try {
				userService.registerUser("admin", "heslo", "ADMIN");
				userService.registerUser("user", "heslo", "TENANT");
				userService.registerUser("user22", "heslo", "TENANT");
			} catch (final IllegalArgumentException exception) {
				System.out.println("Error during user registration: " + exception.getMessage());
			}
		};
	}

	public static void main(final @NotNull String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
