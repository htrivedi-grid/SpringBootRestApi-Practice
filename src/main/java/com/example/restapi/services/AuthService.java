package com.example.restapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.restapi.data.User;
import com.example.restapi.models.UserEntity;
import com.example.restapi.repository.UserRepository;
import com.example.restapi.security.UserDetailsImpl;
import com.example.restapi.security.jwt.JwtUtils;

@Service
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public UserEntity getAuthenticatedUserDetails(UserEntity request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new UserEntity(
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getPassword(),
                jwt);
    }

    public User executeUserSignUp(UserEntity signUpRequest) {

        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return user;
    }

    public boolean userExists(UserEntity signUpRequest) {
        return userRepository.existsByEmail(signUpRequest.getEmail());
    }
}
