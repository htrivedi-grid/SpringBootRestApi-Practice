package com.example.restapi.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi.data.User;
import com.example.restapi.models.Constants;
import com.example.restapi.models.UserCartEntity;
import com.example.restapi.models.UserEntity;
import com.example.restapi.repository.UserRepository;
import com.example.restapi.security.UserDetailsImpl;
import com.example.restapi.security.jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<Object> userLogin(@Validated @RequestBody UserEntity request, Model model,
      HttpSession session) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    List<UserCartEntity> list = (List<UserCartEntity>) session.getAttribute(Constants.SESSION_CART_ATTRIBUTE);

    if (list == null) {
      list = new ArrayList<UserCartEntity>();
    }
    session.setAttribute(Constants.SESSION_CART_ATTRIBUTE, list);

    return ResponseEntity.ok(new UserEntity(
        userDetails.getUsername(),
        userDetails.getEmail(),
        userDetails.getPassword(),
        jwt,
        session.getId()));
  }

  @PostMapping("/signup")
  public ResponseEntity<Object> userSignup(@Validated @RequestBody UserEntity signUpRequest) {
    try {
      if (userRepository.existsByEmail(signUpRequest.getEmail())) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }

      User user = new User(
          signUpRequest.getName(),
          signUpRequest.getEmail(),
          encoder.encode(signUpRequest.getPassword()));

      userRepository.save(user);

      return ResponseEntity.status(HttpStatus.CREATED).body(user);
    } catch (Exception exception) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<Object> postMethodName(HttpServletRequest request) {
    request.getSession().invalidate();
    return ResponseEntity.ok().build();
  }

}
