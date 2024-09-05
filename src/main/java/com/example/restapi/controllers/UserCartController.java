package com.example.restapi.controllers;

import java.util.ArrayList;
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
import org.springframework.web.context.annotation.SessionScope;

import com.example.restapi.exception.BadRequestException;
import com.example.restapi.exception.ResourceNotFoundException;
import com.example.restapi.models.Constants;
import com.example.restapi.models.UserCartEntity;
import com.example.restapi.services.UserCartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart")
public class UserCartController {

    @Autowired
    private UserCartService cartService;

    @SessionScope
    @GetMapping()
    public ResponseEntity<List<UserCartEntity>> getAllCartItems(@RequestParam(name = "user_id") String userId,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<UserCartEntity> list = getCartItemsFromSession(session);

        if (!list.isEmpty()) {
            list = cartService.getCartSummary(list);
        }
        session.setAttribute(Constants.SESSION_CART_ATTRIBUTE, list);
        return ResponseEntity.ok(list);
    }

    @SessionScope
    @PostMapping()
    public ResponseEntity<HttpStatus> addItemToCart(@RequestBody UserCartEntity entity, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<UserCartEntity> list = getCartItemsFromSession(session);

        boolean updateQuantitySuccess = cartService.addCartItemQuantity(list, entity);
        if (!updateQuantitySuccess)
            throw new BadRequestException("Quanity is not allowed, out of stock");

        session.setAttribute(Constants.SESSION_CART_ATTRIBUTE, list);
        return ResponseEntity.ok().build();
    }

    @SessionScope
    @DeleteMapping("/{product_id}")
    public ResponseEntity<HttpStatus> removeItemFromCart(@PathVariable(name = "product_id") String productId,
            @RequestBody UserCartEntity entity, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<UserCartEntity> list = getCartItemsFromSession(session);

        boolean updateQuantitySuccess = cartService.removeCartItemQuantity(productId, list, entity);
        if (!updateQuantitySuccess)
            throw new ResourceNotFoundException("Item not present in the cart");

        session.setAttribute(Constants.SESSION_CART_ATTRIBUTE, list);
        return ResponseEntity.ok().build();
    }

    private List<UserCartEntity> getCartItemsFromSession(HttpSession session) {
        List<UserCartEntity> list = (List<UserCartEntity>) session.getAttribute(Constants.SESSION_CART_ATTRIBUTE);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
}
