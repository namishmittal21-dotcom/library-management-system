package com.library;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.library.model.Role;
import com.library.model.User;
import com.library.repository.UserRepository;

@SpringBootApplication
public class LibraryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
    }

    @Bean
    CommandLineRunner init(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap-admin.username:}") String username,
            @Value("${app.bootstrap-admin.email:}") String email,
            @Value("${app.bootstrap-admin.password:}") String password) {
        return args -> {
            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                return;
            }

            Optional<User> existingAdmin = userRepository.findByUsername(username);
            if (existingAdmin.isPresent()) {
                return;
            }

            User admin = new User();
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setFullName(username);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRole(Role.ROLE_ADMIN);
            userRepository.save(admin);
            System.out.println("Initial administrator account created.");
        };
    }
}
