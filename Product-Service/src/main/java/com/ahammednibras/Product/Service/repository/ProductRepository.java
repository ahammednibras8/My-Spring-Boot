package com.ahammednibras.Product.Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahammednibras.Product.Service.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA will implement this automatically
}
