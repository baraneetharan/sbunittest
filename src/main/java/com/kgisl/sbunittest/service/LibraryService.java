package com.kgisl.sbunittest.service;

import java.util.List;
import java.util.Optional;

import com.kgisl.sbunittest.entity.Library;

public interface LibraryService {
    public List<Library> getAllLibrarys();

    public Optional<Library> getLibraryById(Long id);

    public Library createLibrary(Library library);

    public Library updateLibrary(Long id, Library updated);

    public boolean deleteLibrary(Long id);
}
