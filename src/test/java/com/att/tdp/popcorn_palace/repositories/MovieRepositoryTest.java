package com.att.tdp.popcorn_palace.repositories;


import com.att.tdp.popcorn_palace.models.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
    }

    @Test
    void testExistByTitle_WhenMovieExists_ThenShouldReturnTrue() {
        //given
        String title = "Titanic";
        String genre = "Genre";
        Integer duration = 194;
        BigDecimal rating = BigDecimal.valueOf(7.9);
        String releaseYear = "1997";


        Movie movie = new Movie(title, genre, duration, rating, releaseYear);
        movieRepository.save(movie);

        //when
        boolean expected = movieRepository.existsByTitle(title);

        //then
        assertThat(expected).isTrue();
    }

    @Test
    void testExistByTitle_WhenMovieDoesNotExist_ShouldReturnFalse() {
        //given
        String title = "Titanic";

        //when
        boolean expected = movieRepository.existsByTitle(title);

        //then
        assertThat(expected).isFalse();
    }

    @Test
    void testFindByTitle_WhenMovieExist_ShouldReturnMovie() {
        //given
        String title = "Titanic";
        String genre = "Genre";
        Integer duration = 194;
        BigDecimal rating = BigDecimal.valueOf(7.9);
        String releaseYear = "1997";

        Movie movie = new Movie(title, genre, duration, rating, releaseYear);
        movieRepository.save(movie);

        //when
        Optional<Movie> retrievedMovie = movieRepository.findByTitle(title);

        //then
        assertThat(retrievedMovie).isPresent();
        assertNotNull(retrievedMovie);
        assertEquals(title, retrievedMovie.get().getTitle());
        assertEquals(genre, retrievedMovie.get().getGenre());
        assertEquals(duration, retrievedMovie.get().getDuration());
        assertEquals(rating, retrievedMovie.get().getRating());
        assertEquals(releaseYear, retrievedMovie.get().getReleaseYear());
    }

    @Test
    void testFindByTitle_WhenMovieDoesNotExist_ShouldReturnEmptyOptional() {
        //given
        String title = "Titanic";

        //when
        Optional<Movie> retrievedMovie = movieRepository.findByTitle(title);

        //then
        assertThat(retrievedMovie).isNotPresent();
    }
}
