package com.panda.authappbackend;

import com.panda.authappbackend.configs.AppConstants;
import com.panda.authappbackend.models.Role;
import com.panda.authappbackend.models.User;
import com.panda.authappbackend.repositroies.RoleRepository;
import com.panda.authappbackend.repositroies.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
@RequiredArgsConstructor
public class AuthAppBackendApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(AuthAppBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        roleRepository.findByName(AppConstants.ROLE_ADMIN).ifPresentOrElse(
                role -> {
                    // Role already exists, do nothing
                    System.out.println("Role ADMIN already exists" );
                },
                () -> {
                    // Create ROLE_ADMIN
                    roleRepository.save(
                            Role.builder()
                                    .id(UUID.randomUUID())
                                    .name(AppConstants.ROLE_ADMIN)
                                    .build()
                    );
                }
        );

        roleRepository.findByName(AppConstants.ROLE_USER).ifPresentOrElse(
                role -> {
                    // Role already exists, do nothing
                    System.out.println("Role USER already exists" );
                },
                () -> {
                    // Create ROLE_ADMIN
                    roleRepository.save(
                            Role.builder()
                                    .id(UUID.randomUUID())
                                    .name(AppConstants.ROLE_USER)
                                    .build()
                    );
                }
        );

        Role role = roleRepository.findByName(AppConstants.ROLE_ADMIN).orElse(null);
        if(role == null) {
            throw new RuntimeException("ROLE_ADMIN not found");
        }
        System.out.println("Admin Role: " + role);
        userRepository.findByEmail("admin@gmail.com").ifPresentOrElse(
                user -> {
                    // Admin user already exists, do nothing
                    System.out.println("Admin user already exists" );
                },
                () -> {
                    userRepository.save(
                            User.builder()
                                    .email("admin@gmail.com")
                                    .password(passwordEncoder.encode("admin123")) // In a real application, ensure this password is hashed
                                    .username("Admin")
                                    .enabled(true)
                                    .roles(java.util.Set.of(role))
                                    .build()
                    );
                }
        );
    }
}
