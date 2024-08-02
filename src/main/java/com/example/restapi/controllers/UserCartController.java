package com.example.restapi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Object> getAllCartItems(@RequestParam(name = "user_id") String userId,
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
    public ResponseEntity<Object> addItemToCart(@RequestBody UserCartEntity entity, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            List<UserCartEntity> list = getCartItemsFromSession(session);
            Optional<UserCartEntity> data = list.stream()
                    .filter(item -> item.getProductId().equals(entity.getProductId())).findFirst();

            if (data.isPresent()) {
                UserCartEntity cartItem = data.get();
                if (cartService.hasRequiredQuantity(cartItem.getProductId(),
                        cartItem.getQuantity() + entity.getQuantity())) {
                    cartItem.addQuantity(entity.getQuantity());
                } else
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quanity is not allowed, out of limit");
            } else {
                if (cartService.hasRequiredQuantity(entity.getProductId(), entity.getQuantity())) {
                    list.add(entity);
                } else
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quanity is not allowed, out of limit");
            }
            session.setAttribute(Constants.SESSION_CART_ATTRIBUTE, list);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @SessionScope
    @DeleteMapping("/{product_id}")
    public ResponseEntity<Object> removeItemFromCart(@PathVariable(name = "product_id") String productId,
            @RequestBody UserCartEntity entity, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            List<UserCartEntity> list = getCartItemsFromSession(session);
            Optional<UserCartEntity> data = list.stream()
                    .filter(item -> item.getProductId() == Integer.parseInt(productId)).findFirst();
            if (data.isPresent()) {
                UserCartEntity carItemObj = data.get();
                carItemObj.removeQuantity(entity.getQuantity());
                if (carItemObj.getQuantity() == 0)
                    list.remove(carItemObj);
                session.setAttribute(Constants.SESSION_CART_ATTRIBUTE, list);
                return ResponseEntity.ok().build();
            } else
                return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private List<UserCartEntity> getCartItemsFromSession(HttpSession session) {
        List<UserCartEntity> list = (List<UserCartEntity>) session.getAttribute(Constants.SESSION_CART_ATTRIBUTE);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
}
