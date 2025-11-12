package com.kgisl.sbunittest.service;

import java.util.List;
import java.util.Optional;

import com.kgisl.sbunittest.entity.Movie;

public interface MovieService {
    public List<Movie> getAllMovies();

    public Optional<Movie> getMovieById(Long id);

    public Movie createMovie(Movie movie);

    public Movie updateMovie(Long id, Movie updated);

    public boolean deleteMovie(Long id);
}
