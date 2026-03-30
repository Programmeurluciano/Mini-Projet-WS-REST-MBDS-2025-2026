package com.miniprojet.wsrest.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.miniprojet.wsrest.model.Role;
import com.miniprojet.wsrest.model.User;
import com.miniprojet.wsrest.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {

        return args -> {

            if(userRepository.findByEmail("admin@admin.com").isEmpty()) {

                User admin = new User();
                admin.setFullname("Admin");
                admin.setEmail("admin@admin.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEnabled(true);
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("Admin créé dans la base !");
            }
        };
    }
}
