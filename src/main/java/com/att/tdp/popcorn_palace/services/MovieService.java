package com.att.tdp.popcorn_palace.services;

import com.att.tdp.popcorn_palace.exceptions.InvalidMovieException;
import com.att.tdp.popcorn_palace.exceptions.MovieAlreadyExistsException;
import com.att.tdp.popcorn_palace.exceptions.MovieNotFoundException;
import com.att.tdp.popcorn_palace.models.Movie;
import com.att.tdp.popcorn_palace.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie createMovie(Movie movie) {
        // Check if Movie with this title already exists in the DB
        if (movieRepository.existsByTitle(movie.getTitle())) {
            throw new MovieAlreadyExistsException(movie.getTitle());
        }

        // Check if Movie title is an actual string
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
            throw new InvalidMovieException("Movie Title cannot be empty");
        }
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        // Find and retrieve all movies from DB
        return movieRepository.findAll();
    }

    public void updateMovie(String movieTitle, Movie movieUpdated) {
        // Check if there is a movie with the given title in the DB
        Movie movie = movieRepository.findByTitle(movieTitle)
                .orElseThrow(() -> new MovieNotFoundException(movieTitle));

        // Check if the title in the request is a title of a movie that already exists in the DB
        if (movieRepository.existsByTitle(movieUpdated.getTitle())) {
            throw new MovieAlreadyExistsException(movieUpdated.getTitle());
        }

        // update movie values with the request
        movie.setTitle(movieUpdated.getTitle());
        movie.setGenre(movieUpdated.getGenre());
        movie.setDuration(movieUpdated.getDuration());
        movie.setRating(movieUpdated.getRating());
        movie.setReleaseYear(movieUpdated.getReleaseYear());
        movieRepository.save(movie);
    }

    public void deleteMovie(String movieTitle) {
        Movie movie = movieRepository.findByTitle(movieTitle)
                .orElseThrow(() -> new MovieNotFoundException(movieTitle));
        movieRepository.delete(movie);
    }
}
