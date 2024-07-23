package com.example.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restapi.data.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
