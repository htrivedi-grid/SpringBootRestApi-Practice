package com.example.restapi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restapi.data.Cart;
import com.example.restapi.data.Inventory;
import com.example.restapi.repository.CartRepository;
import com.example.restapi.repository.InventoryRepository;

@Service
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
