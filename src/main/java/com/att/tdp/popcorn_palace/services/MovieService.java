package com.att.tdp.popcorn_palace.services;

import com.att.tdp.popcorn_palace.dtos.MovieRequest;
import com.att.tdp.popcorn_palace.dtos.MovieResponse;
import com.att.tdp.popcorn_palace.exceptions.MovieInvalidInput;
import com.att.tdp.popcorn_palace.exceptions.MovieAlreadyExistsException;
import com.att.tdp.popcorn_palace.exceptions.MovieNotFoundException;
import com.att.tdp.popcorn_palace.models.Movie;
import com.att.tdp.popcorn_palace.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    public MovieResponse createMovie(MovieRequest movieRequest) {
        // Check if Movie with this title already exists in the DB
        if (movieRepository.existsByTitle(movieRequest.getTitle())) {
            throw new MovieAlreadyExistsException(movieRequest.getTitle());
        }

        // Check if Movie title is an actual string
        if (movieRequest.getTitle() == null || movieRequest.getTitle().trim().isEmpty()) {
            throw new MovieInvalidInput("Movie Title cannot be empty");
        }
        Movie movie = convertMovieRequestToMovie(movieRequest);
        movieRepository.save(movie);
        return convertMovieToMovieResponse(movie);
    }

    public List<MovieResponse> getAllMovies() {
        // Find and retrieve all movies from DB
        return movieRepository.findAll().stream()
                .map(this::convertMovieToMovieResponse)
                .collect(Collectors.toList());
    }

    public void updateMovie(String movieTitle, MovieRequest movieRequest) {
        // Check if there is a movie with the given title in the DB
        Movie movie = movieRepository.findByTitle(movieTitle)
                .orElseThrow(() -> new MovieNotFoundException(movieTitle));

        // Check if the title in the request is a title of a movie that already exists in the DB
        if (movieRepository.existsByTitle(movieRequest.getTitle())) {
            throw new MovieAlreadyExistsException(movieRequest.getTitle());
        }


        // Update movie with the values received in the request
        movie.setTitle(movieRequest.getTitle());
        if (movieRequest.getGenre() != null) {
            movie.setGenre(movieRequest.getGenre());
        }
        if (movieRequest.getDuration() != null) {
            movie.setDuration(movieRequest.getDuration());
        }
        if (movieRequest.getRating() != null) {
            movie.setRating(movieRequest.getRating());
        }
        if (movieRequest.getReleaseYear() != null) {
            movie.setReleaseYear(movieRequest.getReleaseYear());
        }
        movieRepository.save(movie);
    }

    public void deleteMovie(String movieTitle) {
        // Delete movie by title
        Movie movie = movieRepository.findByTitle(movieTitle)
                .orElseThrow(() -> new MovieNotFoundException(movieTitle));
        movieRepository.delete(movie);
    }

    private MovieResponse convertMovieToMovieResponse(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getGenre(),
                movie.getDuration(),
                movie.getRating(),
                movie.getReleaseYear()
        );
    }

    private Movie convertMovieRequestToMovie(MovieRequest movieRequest) {
        return new Movie(
                movieRequest.getTitle(),
                movieRequest.getGenre(),
                movieRequest.getDuration(),
                movieRequest.getRating(),
                movieRequest.getReleaseYear()
        );
    }
}
