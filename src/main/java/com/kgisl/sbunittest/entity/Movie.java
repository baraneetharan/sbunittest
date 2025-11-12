package com.kgisl.sbunittest.entity;

import org.springframework.data.annotation.Id;

public record Movie(@Id Long id, String title, String director, int releaseYear, String genre, double rating) {
    
}
