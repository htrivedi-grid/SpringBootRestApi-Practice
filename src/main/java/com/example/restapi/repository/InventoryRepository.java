package com.example.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.restapi.data.Inventory;

import jakarta.transaction.Transactional;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> findByProductId(Integer productId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Inventory i SET i.quantity = i.quantity - :quantity where i.productId = :productId")
    void updateProductQuantity(Integer productId, Integer quantity);
}
