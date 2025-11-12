package com.kgisl.sbunittest.repository;

// import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

import com.kgisl.sbunittest.entity.Product;

public interface ProductRepository extends ListCrudRepository<Product, Long> {
    
}
