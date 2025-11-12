package com.kgisl.sbunittest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kgisl.sbunittest.entity.Library;
import com.kgisl.sbunittest.service.LibraryService;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/librarys")
@CrossOrigin(origins = "*")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public ResponseEntity<List<Library>> getAll() {
        return ResponseEntity.ok(libraryService.getAllLibrarys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Library> getById(@PathVariable Long id) {
        return libraryService.getLibraryById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Library> create(@RequestBody Library library) {
        Library saved = libraryService.createLibrary(library);
        String location = String.format("/api/librarys/%d", saved.id());
        return ResponseEntity.created(URI.create(location)).body(saved);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Library> update(@PathVariable Long id, @RequestBody Library updatedLibrary) {
        Library library = libraryService.updateLibrary(id, updatedLibrary);
        if (library == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(library, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = libraryService.deleteLibrary(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
