package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.User;
import com.example.demo.repository.UserRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userService;

    @PostMapping("/auth")
    public ResponseEntity<Object> userLogin(@RequestBody UserEntity entity) {
        List<User> users = userService.findByEmailAndPassword(entity.getEmail(), entity.getPassword());
        if (!users.isEmpty())
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/create")
    public ResponseEntity<Object> userSignup(@RequestBody UserEntity entity) {
        try {
            User savedUser = userService.save(new User(entity.getName(), entity.getEmail(), entity.getPassword()));
            // TODO return auth_token
            savedUser.setPassword("");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
