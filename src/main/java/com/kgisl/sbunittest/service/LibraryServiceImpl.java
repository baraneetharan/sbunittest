package com.kgisl.sbunittest.service;

import org.springframework.stereotype.Service;

import com.kgisl.sbunittest.entity.Library;
import com.kgisl.sbunittest.repository.LibraryRepository;
import com.kgisl.sbunittest.Loggable;

import java.util.List;
import java.util.Optional;

@Loggable
@Service
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;

    public LibraryServiceImpl(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    @Override
    public List<Library> getAllLibrarys() {
        return libraryRepository.findAll();
    }

    @Override
    public Optional<Library> getLibraryById(Long id) {
        return libraryRepository.findById(id);
    }

    @Override
    public Library createLibrary(Library library) {
        // library.id(0L);
        return libraryRepository.save(library);
    }

    @Override
    public Library updateLibrary(Long id, Library updated) {
        Library existing = libraryRepository.findById(id).orElse(null);
        if (existing != null) {
            Library toSave = new Library(id, updated.name(), updated.location(), updated.bookCount(), updated.librarian(),updated.isPublic());
            return libraryRepository.save(toSave);
        }
        return null;

    }

    @Override
    public boolean deleteLibrary(Long id) {
        if (libraryRepository.existsById(id)) {
            libraryRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
