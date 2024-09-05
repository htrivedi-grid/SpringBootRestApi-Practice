package com.example.restapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.restapi.data.Inventory;
import com.example.restapi.data.Product;
import com.example.restapi.models.UserCartEntity;
import com.example.restapi.repository.InventoryRepository;
import com.example.restapi.repository.ProductRepository;

@Service
public class UserCartService {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ProductRepository productRepository;

    public boolean addCartItemQuantity(List<UserCartEntity> list, UserCartEntity entity) {
        Optional<UserCartEntity> data = list.stream().filter(item -> item.getProductId().equals(entity.getProductId()))
                .findFirst();

        if (data.isPresent()) {
            UserCartEntity cartItem = data.get();
            if (hasRequiredQuantity(cartItem.getProductId(),
                    cartItem.getQuantity() + entity.getQuantity())) {
                cartItem.addQuantity(entity.getQuantity());
            } else
                return false;
        } else {
            if (hasRequiredQuantity(entity.getProductId(), entity.getQuantity())) {
                list.add(entity);
            } else
                return false;
        }
        return true;
    }

    public boolean removeCartItemQuantity(String productId, List<UserCartEntity> list, UserCartEntity entity) {
        Optional<UserCartEntity> data = list.stream()
                .filter(item -> item.getProductId() == Integer.parseInt(productId)).findFirst();
        if (data.isPresent()) {
            UserCartEntity carItemObj = data.get();
            carItemObj.removeQuantity(entity.getQuantity());
            if (carItemObj.getQuantity() == 0)
                list.remove(carItemObj);
            return true;
        } else
            return false;
    }

    public boolean hasRequiredQuantity(Integer productId, Integer quantity) {
        Optional<Inventory> item = inventoryRepository.findByProductId(productId);
        if (item.isPresent()) {
            return quantity <= item.get().getQuantity();
        }
        return false;
    }

    public List<UserCartEntity> getCartSummary(List<UserCartEntity> items) {
        List<Integer> productIds = items.stream().map(item -> item.getProductId()).toList();
        List<Product> list = productRepository.findAllProductDetailsById(productIds);

        items.forEach(cartItem -> {
            Product product = list.stream().filter(data -> data.getId().equals(cartItem.getProductId())).findFirst()
                    .get();
            cartItem.setTotalAmount(cartItem.getQuantity() * product.getPrice());
        });
        return items;
    }

    public void updateInventoryQuantity(List<UserCartEntity> list) {
        for (UserCartEntity cartItem : list) {
            inventoryRepository.updateProductQuantity(cartItem.getProductId(), cartItem.getQuantity());
        }
    }
}
