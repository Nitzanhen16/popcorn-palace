package com.att.tdp.popcorn_palace.services;

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
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public void updateMovie(String movieTitle, Movie movieUpdated) {
        Movie movie = movieRepository.findByTitle(movieTitle);

        movie.setTitle(movieUpdated.getTitle());
        movie.setGenre(movieUpdated.getGenre());
        movie.setDuration(movieUpdated.getDuration());
        movie.setRating(movieUpdated.getRating());
        movie.setReleaseYear(movieUpdated.getReleaseYear());
        movieRepository.save(movie);
    }

    public void deleteMovie(String movieTitle) {
        Movie movie = movieRepository.findByTitle(movieTitle);
        movieRepository.deleteById(movie.getId());
    }
}
