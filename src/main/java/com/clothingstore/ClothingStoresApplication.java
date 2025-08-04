package com.clothingstore;

import com.clothingstore.model.User;
import com.clothingstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class ClothingStoresApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ClothingStoresApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@store.com").isEmpty()) {
            User admin = User.builder()
                    .name("Admin")
                    .email("admin@store.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("Default admin created: admin@store.com / admin123");
        }
    }
}
