package com.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entity.User;
import com.project.repository.UserRepository;
import com.project.security.JwtUtils;


@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    public User getUserFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            logger.warn("Invalid token format");
            return null;  // Token is missing or invalid
        }

        String jwtToken = token.substring(7);  // Remove "Bearer " prefix

        if (jwtUtils.isTokenExpired(jwtToken)) {
            logger.warn("Token has expired");
            return null;  // Token has expired
        }

        String email = jwtUtils.extractUsername(jwtToken);  // Extract the email from the token

        if (email == null) {
            logger.warn("Invalid JWT token");
            return null;  // Invalid JWT token
        }

        return userRepository.findByEmail(email).orElse(null);  // Find user by email
    }
}