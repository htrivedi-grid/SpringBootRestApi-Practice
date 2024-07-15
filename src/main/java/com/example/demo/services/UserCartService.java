package com.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.data.Cart;
import com.example.demo.data.Inventory;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.InventoryRepository;

public class UserCartService {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    CartRepository cartRepository;

    public void addItemToCart(Cart cartItem) {
        Optional<Inventory> item = inventoryRepository.findByProductId(cartItem.getProductId());
        if (item.isPresent()) {
            if (item.get().getQuantity() > 0)
                cartRepository.save(cartItem);
        }
    }
}
