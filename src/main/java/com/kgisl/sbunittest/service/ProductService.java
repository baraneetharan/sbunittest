package com.kgisl.sbunittest.service;

import java.util.List;
import java.util.Optional;

import com.kgisl.sbunittest.entity.Product;

public interface ProductService {
    public List<Product> getAllProducts();
    public Optional<Product> getProductById(Long id);
    public Product createProduct(Product product);
    public Product updateProduct(Long id, Product updated);
    public boolean deleteProduct(Long id);
}
