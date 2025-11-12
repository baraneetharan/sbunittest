package com.kgisl.sbunittest.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.kgisl.sbunittest.entity.Library;

public interface LibraryRepository extends ListCrudRepository<Library, Long> {
    
}
