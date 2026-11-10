
package com.carmarketplace.service;

import com.carmarketplace.dto.RegisterRequest;
import com.carmarketplace.entity.User;
import com.carmarketplace.repository.UserRepository;
import com.carmarketplace.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.carmarketplace.common.exception.ResourceNotFoundException;
import com.carmarketplace.common.exception.InvalidCredentialsException;

@Service
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserService userService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(RegisterRequest request) {

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return userService.register(user);
    }

    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
