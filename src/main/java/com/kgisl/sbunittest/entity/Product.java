package com.kgisl.sbunittest.entity;

import org.springframework.data.annotation.Id;

public record Product(@Id Long id, String name, String category, double price, boolean available) {
    
}
