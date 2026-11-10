
package com.carmarketplace.controller;

import com.carmarketplace.dto.AuthResponse;
import com.carmarketplace.dto.LoginRequest;
import com.carmarketplace.dto.RegisterRequest;
import com.carmarketplace.entity.User;
import com.carmarketplace.security.JwtService;
import com.carmarketplace.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(
            AuthService authService,
            JwtService jwtService
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        User user = authService.register(request);

        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        String token = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
