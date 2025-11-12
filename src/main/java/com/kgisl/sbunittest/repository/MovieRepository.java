package com.kgisl.sbunittest.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.kgisl.sbunittest.entity.Movie;

public interface MovieRepository extends ListCrudRepository<Movie, Long> {
    
}
