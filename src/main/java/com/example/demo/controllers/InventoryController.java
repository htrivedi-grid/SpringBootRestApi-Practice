package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.Inventory;
import com.example.demo.repository.InventoryRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryService;

    @GetMapping()
    public ResponseEntity<Object> getAllInventory() {
        List<Inventory> list = inventoryService.findAll();
        return ResponseEntity.ok(list);
    }

    @PostMapping()
    public ResponseEntity<Object> saveInventory(@RequestBody Inventory entity) {
        try {
            inventoryService.save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{product_id}")
    public ResponseEntity<Object> updateQuantity(@PathVariable(name = "product_id") String productId,
            @RequestBody Inventory entity) {
        Optional<Inventory> item = inventoryService.findByProductId(Integer.parseInt(productId));
        if (item.isPresent()) {
            try {
                Inventory data = item.get();
                data.setQuantity(entity.getQuantity());
                inventoryService.save(data);
                return ResponseEntity.ok().build();
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
