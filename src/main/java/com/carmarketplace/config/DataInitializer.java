package com.carmarketplace.config;

import com.carmarketplace.entity.Role;
import com.carmarketplace.entity.enums.RoleName;
import com.carmarketplace.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.carmarketplace.entity.User;
import com.carmarketplace.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import java.util.Set;
import org.springframework.core.annotation.Order;

@Configuration
public class DataInitializer {
    @Bean
    @Order(1)
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {

            for (RoleName roleName : RoleName.values()) {

                if (roleRepository.findByName(roleName).isEmpty()) {

                    Role role = new Role();
                    role.setName(roleName);

                    roleRepository.save(role);
                }
            }
        };
    }
    @Bean
    @Order(2)
    CommandLineRunner initAdmin(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            @Value("${admin.email}") String adminEmail,
            @Value("${admin.password}") String adminPassword,
            @Value("${admin.first-name}") String firstName,
            @Value("${admin.last-name}") String lastName
    ) {
        return args -> {

            if (userRepository.existsByEmail(adminEmail)) {
                return;
            }

            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            User admin = new User();
            admin.setFirstName(firstName);
            admin.setLastName(lastName);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
        };
    }
}