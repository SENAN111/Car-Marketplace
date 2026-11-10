package com.carmarketplace.controller;

import com.carmarketplace.dto.UserProfileResponse;
import com.carmarketplace.entity.User;
import com.carmarketplace.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserProfileResponse getMyProfile(Authentication authentication) {

        String email = authentication.getName();

        User user = userService.findByEmail(email);

        return new UserProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}