package com.att.tdp.popcorn_palace.contollers;

import com.att.tdp.popcorn_palace.models.Movie;
import com.att.tdp.popcorn_palace.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public Movie addMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @PostMapping("/update/{movieTitle}")
    public void updateMovie(@PathVariable String movieTitle, @RequestBody Movie movie) {
        movieService.updateMovie(movieTitle, movie);
    }

    @DeleteMapping("/{movieTitle}")
    public void deleteMovie(@PathVariable String movieTitle) {
        movieService.deleteMovie(movieTitle);
    }
}
