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

    public User register(User user) {

        if (emailExists(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Default role not found"));

        user.setRoles(Set.of(userRole));

        return userRepository.save(user);
    }
}
