package org.airtribe.employeetracking.service;

import org.airtribe.employeetracking.entity.Employee;
import org.airtribe.employeetracking.entity.User;
import org.airtribe.employeetracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
@Autowired
private UserRepository userRepository;

    public String getRoleByEmail(String email) {
        // Find the user by email
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found with Email: " + email));
        return user != null ? user.getRole().name() : null;
    }

    public User getUserByAuthentication(Authentication authentication) throws IllegalArgumentException
    {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        Map<String, Object> claims = jwtToken.getToken().getClaims();
        String userEmail = (String) claims.get("email");
        User userAu =  userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new IllegalArgumentException("User not found with Email: " + userEmail));
        return userAu;
    }

}
