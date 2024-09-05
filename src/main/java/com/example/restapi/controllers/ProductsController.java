package com.example.restapi.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi.data.Product;
import com.example.restapi.exception.ResourceNotFoundException;
import com.example.restapi.repository.ProductListRepository;
import com.example.restapi.repository.ProductRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/product")
public class ProductsController {

    @Autowired
    private ProductListRepository productListService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping()
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "10") String limit) {
        PageRequest pageRequest = PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit));
        Page<Product> list = productListService.findAll(pageRequest);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(Integer.parseInt(id));
        if (product.isPresent())
            return ResponseEntity.ok(product.get());
        else
            throw new ResourceNotFoundException("Product not found");
    }

    @PostMapping()
    public ResponseEntity<Product> saveProduct(@RequestBody Product entity) throws Exception {
        try {
            Product savedProduct = productRepository.save(entity);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception exception) {
            throw new Exception("Couldn't save the Product");
        }
    }
}
