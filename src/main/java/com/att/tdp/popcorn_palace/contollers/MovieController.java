package com.att.tdp.popcorn_palace.contollers;

import com.att.tdp.popcorn_palace.dtos.MovieRequest;
import com.att.tdp.popcorn_palace.dtos.MovieResponse;
import com.att.tdp.popcorn_palace.services.MovieService;
import jakarta.validation.Valid;
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

    @GetMapping("/all")
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public MovieResponse addMovie(@Valid @RequestBody MovieRequest movieRequest) {
        return movieService.createMovie(movieRequest);
    }

    @PostMapping("/update/{movieTitle}")
    public void updateMovie(@PathVariable String movieTitle, @Valid @RequestBody MovieRequest movieRequest) {
        movieService.updateMovie(movieTitle, movieRequest);
    }

    @DeleteMapping("/{movieTitle}")
    public void deleteMovie(@PathVariable String movieTitle) {
        movieService.deleteMovie(movieTitle);
    }
}
