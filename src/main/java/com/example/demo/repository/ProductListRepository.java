package com.example.demo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.example.demo.data.Product;

public interface ProductListRepository extends PagingAndSortingRepository<Product, Integer> {

}