package com.example.restapi.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.restapi.data.Product;

public interface ProductListRepository extends PagingAndSortingRepository<Product, Integer> {

}