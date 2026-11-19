package com.carmarketplace.service;

import com.carmarketplace.common.exception.EmailAlreadyExistsException;
import com.carmarketplace.common.exception.ResourceNotFoundException;
import com.carmarketplace.entity.User;
import com.carmarketplace.entity.Role;
import com.carmarketplace.entity.enums.RoleName;
import com.carmarketplace.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import com.carmarketplace.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }


    public User register(User user, String selectedRole) {

        if (emailExists(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (!selectedRole.equals("USER") && !selectedRole.equals("SELLER")) {
            throw new IllegalArgumentException("Invalid role selection");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        RoleName roleName = selectedRole.equals("SELLER")
                ? RoleName.ROLE_SELLER
                : RoleName.ROLE_USER;

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found"));

        user.setRoles(Set.of(role));

        return userRepository.save(user);
    }

}
