package com.example.demo.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.Cart;
import com.example.demo.repository.CartRepository;

@RestController
@RequestMapping("/cart")
public class UserCartController {

    @Autowired
    private CartRepository cartService;

    @GetMapping()
    public ResponseEntity<Object> getAllCartItems(@RequestParam(name = "user_id") String userId) {
        List<Cart> list = cartService.findByUserId(Integer.parseInt(userId));
        return ResponseEntity.ok(list);
    }

    @PostMapping()
    public ResponseEntity<Object> addItemToCart(@RequestBody Cart entity) {
        try {
            cartService.save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeItemFromCart(@PathVariable(name = "id") String id) {
        try {
            cartService.deleteById(Integer.parseInt(id));
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
