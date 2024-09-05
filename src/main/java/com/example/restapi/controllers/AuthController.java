package com.example.restapi.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi.data.User;
import com.example.restapi.exception.BadRequestException;
import com.example.restapi.models.Constants;
import com.example.restapi.models.UserCartEntity;
import com.example.restapi.models.UserEntity;
import com.example.restapi.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  AuthService authService;

  @PostMapping("/signin")
  public ResponseEntity<UserEntity> userLogin(@Validated @RequestBody UserEntity request, Model model,
      HttpSession session) {

    UserEntity userDetails = authService.getAuthenticatedUserDetails(request);

    List<UserCartEntity> list = (List<UserCartEntity>) session.getAttribute(Constants.SESSION_CART_ATTRIBUTE);
    if (list == null) {
      list = new ArrayList<UserCartEntity>();
    }
    session.setAttribute(Constants.SESSION_CART_ATTRIBUTE, list);

    userDetails.setSessionId(session.getId());
    return ResponseEntity.ok(userDetails);
  }

  @PostMapping("/signup")
  public ResponseEntity<User> userSignup(@Validated @RequestBody UserEntity signUpRequest) {
    if (authService.userExists(signUpRequest)) {
      throw new BadRequestException("User already exists with same email");
    }

    User newUser = authService.executeUserSignUp(signUpRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
  }

  @PostMapping("/logout")
  public ResponseEntity<HttpStatus> postMethodName(HttpServletRequest request) {
    request.getSession().invalidate();
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
