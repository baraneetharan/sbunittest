package com.kgisl.sbunittest.entity;

import org.springframework.data.annotation.Id;

public record Library(@Id Long id, String name, String location, int bookCount, String librarian, boolean isPublic) {
    
}
