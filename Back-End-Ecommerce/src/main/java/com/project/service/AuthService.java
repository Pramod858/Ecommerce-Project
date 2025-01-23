package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entity.User;
import com.project.repository.UserRepository;
import com.project.security.JwtUtils;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    public User getUserFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return null;  // Token is missing or invalid
        }

        String jwtToken = token.substring(7);  // Remove "Bearer " prefix
        
        if (jwtUtils.isTokenExpired(jwtToken)) {
        	return null;
        }
        
        String email = jwtUtils.extractUsername(jwtToken);  // Extract the email from the token

        if (email == null) {
            return null;  // Invalid JWT token
        }

        return userRepository.findByEmail(email).orElse(null);  // Find user by email
    }
}

