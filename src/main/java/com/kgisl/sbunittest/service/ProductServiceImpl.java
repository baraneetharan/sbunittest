package com.kgisl.sbunittest.service;

import org.springframework.stereotype.Service;

import com.kgisl.sbunittest.entity.Product;
import com.kgisl.sbunittest.repository.ProductRepository;
import com.kgisl.sbunittest.Loggable;

import java.util.List;
import java.util.Optional;

@Loggable
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        // product.id(0L);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updated) {
        Product existing = productRepository.findById(id).orElse(null);
        if (existing != null) {
            Product toSave = new Product(id, updated.name(), updated.category(), updated.price(), updated.available());
            return productRepository.save(toSave);
        }
        return null;

    }

    @Override
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
