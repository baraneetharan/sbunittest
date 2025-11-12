package com.kgisl.sbunittest.service;

import org.springframework.stereotype.Service;

import com.kgisl.sbunittest.entity.Movie;
import com.kgisl.sbunittest.repository.MovieRepository;
import com.kgisl.sbunittest.Loggable;

import java.util.List;
import java.util.Optional;

@Loggable
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    @Override
    public Movie createMovie(Movie movie) {
        // movie.id(0L);
        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovie(Long id, Movie updated) {
        Movie existing = movieRepository.findById(id).orElse(null);
        if (existing != null) {
            Movie toSave = new Movie(id, updated.title(), updated.director(), updated.releaseYear(), updated.genre(),updated.rating());
            return movieRepository.save(toSave);
        }
        return null;

    }

    @Override
    public boolean deleteMovie(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
