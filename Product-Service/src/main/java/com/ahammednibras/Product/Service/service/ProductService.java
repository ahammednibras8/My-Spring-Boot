package com.ahammednibras.Product.Service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ahammednibras.Product.Service.model.Product;
import com.ahammednibras.Product.Service.repository.ProductRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public ProductService(ProductRepository productRepository, RestTemplate restTemplate) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackGetStock")
    public int getStock(Long productId) {
        String inventoryUrl = "http://localhost:8081/inventory/" + productId;
        System.out.println("[ProductService] Attempting to fetch stock for product ID: " + productId);
        try {
            return restTemplate.getForObject(inventoryUrl, Integer.class);
        } catch (Exception e) {
            System.err.println(
                    "[ProductService] Error fetching stock for product ID " + productId + ": " + e.getMessage());
            throw e; // Important: Re-throw the exception
        }
    }

    public int fallbackGetStock(Long productId, Throwable throwable) {
        System.err.println("[ProductService] FallbackGetStock method called for product ID: " + productId + ". Reason: "
                + throwable.getMessage());
        return 0; // Default stock when service is down
    }

    public Product getProductWithStock(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(product -> {
            try {
                Integer stock = getStock(id);
                product.setStock(stock);
            } catch (Exception e) {
                System.err.println("[ProductService] Error in getProductWithStock while calling getStock: " + e.getMessage());
                product.setStock(-1);
            }
            return product;
        }).orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            return productRepository.save(product);
        }
        return null;
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
