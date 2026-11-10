package com.carmarketplace.config;

import com.carmarketplace.entity.Role;
import com.carmarketplace.entity.enums.RoleName;
import com.carmarketplace.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
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
}