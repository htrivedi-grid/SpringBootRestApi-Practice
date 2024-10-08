package com.example.restapi.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi.data.Inventory;
import com.example.restapi.exception.BadRequestException;
import com.example.restapi.exception.ResourceNotFoundException;
import com.example.restapi.repository.InventoryRepository;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryService;

    @GetMapping()
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> list = inventoryService.findAll();
        return ResponseEntity.ok(list);
    }

    @PostMapping()
    public ResponseEntity<Inventory> saveInventory(@RequestBody Inventory entity) {
        try {
            Inventory savedEntity = inventoryService.save(entity);
            return new ResponseEntity<Inventory>(savedEntity, HttpStatus.CREATED);
        } catch (Exception exception) {
            throw new BadRequestException("Couldn't save the data");
        }
    }

    @PutMapping("/{product_id}")
    public ResponseEntity<HttpStatus> updateQuantity(@PathVariable(name = "product_id") String productId,
            @RequestBody Inventory entity) {
        Optional<Inventory> item = inventoryService.findByProductId(Integer.parseInt(productId));
        if (item.isPresent()) {
            try {
                Inventory data = item.get();
                data.setQuantity(entity.getQuantity());
                inventoryService.save(data);
                return ResponseEntity.ok().build();
            } catch (Exception exception) {
                throw new BadRequestException("Coudn't update quantity for given product_id");
            }
        } else {
            throw new ResourceNotFoundException("Product not found");
        }
    }
}
