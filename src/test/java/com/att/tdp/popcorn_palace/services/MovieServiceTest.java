package com.att.tdp.popcorn_palace.services;

import com.att.tdp.popcorn_palace.dtos.MovieRequest;
import com.att.tdp.popcorn_palace.dtos.MovieResponse;
import com.att.tdp.popcorn_palace.exceptions.MovieAlreadyExistsException;
import com.att.tdp.popcorn_palace.exceptions.MovieNotFoundException;
import com.att.tdp.popcorn_palace.models.Movie;
import com.att.tdp.popcorn_palace.repositories.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @Test
    void testGetMovieEntityById_WhenMovieExists_ThenShouldMovie() {
        //given
        Long movieId = 1L;
        String title = "Titanic";
        String genre = "Drama";
        Integer duration = 194;
        BigDecimal rating = BigDecimal.valueOf(7.9);
        String releaseYear = "1997";

        Movie expectedMovie = new Movie(title, genre, duration, rating, releaseYear);
        expectedMovie.setId(movieId);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(expectedMovie));

        //when
        Movie actualMovie = movieService.getMovieEntityById(movieId);

        //then
        assertNotNull(actualMovie);
        assertEquals(movieId, actualMovie.getId());
        assertEquals(title, actualMovie.getTitle());
        assertEquals(genre, actualMovie.getGenre());
        assertEquals(duration, actualMovie.getDuration());
        assertEquals(rating, actualMovie.getRating());
        assertEquals(releaseYear, actualMovie.getReleaseYear());

        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void testGetMovieEntityById_WhenMovieDoesNotExist_ShouldThrowMovieNotFoundException() {
        //given
        Long movieId = 99L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        //when & then
        MovieNotFoundException thrown = assertThrows(MovieNotFoundException.class, () -> movieService.getMovieEntityById(movieId));

        assertEquals(("Could not find movie with ID " + movieId), thrown.getMessage());
        verify(movieRepository,times(1)).findById(movieId);
    }

    @Test
    void testCreateMovie_WhenMovieIsCreatedSuccessfully_ShouldReturnMovieResponse() {
        //given
        String title = "Titanic";
        String genre = "Drama";
        Integer duration = 194;
        BigDecimal rating = BigDecimal.valueOf(7.9);
        String releaseYear = "1997";
        MovieRequest movieRequest = new MovieRequest(title, genre, duration, rating, releaseYear);

        Movie savedMovie = new Movie(title, genre, duration, rating, releaseYear);

        when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);

        //when
        MovieResponse movieResponse = movieService.createMovie(movieRequest);

        //then
        assertNotNull(movieResponse);
        assertEquals(savedMovie.getId(), movieResponse.getId());
        assertEquals(movieRequest.getTitle(), movieResponse.getTitle());
        assertEquals(movieRequest.getGenre(), movieResponse.getGenre());
        assertEquals(movieRequest.getDuration(), movieResponse.getDuration());
        assertEquals(movieRequest.getRating(), movieResponse.getRating());
        assertEquals(movieRequest.getReleaseYear(), movieResponse.getReleaseYear());

        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void testCreateMovie_WhenMovieAlreadyExists_ShouldThrowMovieAlreadyExistsException() {
        //given
        String title = "Titanic";
        String genre = "Drama";
        Integer duration = 194;
        BigDecimal rating = BigDecimal.valueOf(7.9);
        String releaseYear = "1997";
        MovieRequest movieRequest = new MovieRequest(title, genre, duration, rating, releaseYear);

        when(movieRepository.existsByTitle(title)).thenReturn(true);

        //when & then
        MovieAlreadyExistsException thrown = assertThrows(MovieAlreadyExistsException.class, () -> movieService.createMovie(movieRequest));

        assertEquals(("A movie with the title '" + title + "' already exists."), thrown.getMessage());

        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testGetAllMovies_WhenMoviesExist_ShouldReturnMovieResponseList() {
        //given
        List<Movie> movies = List.of(new Movie("Titanic", "Drama", 194, BigDecimal.valueOf(100), "1997"), new Movie("Inception", "Sci-Fi", 148, BigDecimal.valueOf(8.8), "2010"));
        when(movieRepository.findAll()).thenReturn(movies);

        // when
        List<MovieResponse> result = movieService.getAllMovies();

        //then
        assertNotNull(result);
        assertEquals(movies.size(), result.size());
        assertEquals("Titanic", result.getFirst().getTitle());
        assertEquals("Inception", result.get(1).getTitle());

        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testGetAllMovies_WhenNoMoviesExist_ShouldReturnEmptyList() {
        //given
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        List<MovieResponse> result = movieService.getAllMovies();

        //then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testUpdateMovie_WhenMovieExists_ShouldReturnUpdatedMovie() {
        //given
        String existingTitle = "Titanic";
        String genre = "Drama";
        Integer existingDuration = 194;
        BigDecimal existingRating = BigDecimal.valueOf(7.9);
        String existingReleaseYear = "1997";
        Movie existingMovie = new Movie(existingTitle, genre, existingDuration, existingRating, existingReleaseYear);

        String updatedTitle = "Titanic 2";
        Integer updatedDuration = 194;
        BigDecimal updatedRating = BigDecimal.valueOf(8.1);
        String updatedReleaseYear = "1999";
        MovieRequest movieRequest = new MovieRequest(updatedTitle, genre, updatedDuration, updatedRating, updatedReleaseYear);
        Movie updatedMovie = new Movie(updatedTitle, genre, updatedDuration, updatedRating, updatedReleaseYear);

        when(movieRepository.findByTitle(existingTitle)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(updatedMovie);

        //when
        movieService.updateMovie(existingTitle, movieRequest);

        //then
        assertEquals(updatedTitle, existingMovie.getTitle());
        assertEquals(genre, existingMovie.getGenre());
        assertEquals(updatedDuration, existingMovie.getDuration());
        assertEquals(updatedRating, existingMovie.getRating());
        assertEquals(updatedReleaseYear, existingMovie.getReleaseYear());

        verify(movieRepository, times(1)).findByTitle(existingTitle);
        verify(movieRepository, times(1)).save(existingMovie);
    }

    @Test
    void testUpdateMovie_WhenMovieDoesNotExist_ShouldThrowMovieNotFoundException() {
        //given
        String title = "Titanic";
        String genre = "Drama";
        Integer duration = 194;
        BigDecimal rating = BigDecimal.valueOf(7.9);
        String releaseYear = "1997";
        MovieRequest movieRequest = new MovieRequest(title, genre, duration, rating, releaseYear);

        when(movieRepository.findByTitle(title)).thenReturn(Optional.empty());

        //when & then
        MovieNotFoundException thrown = assertThrows(MovieNotFoundException.class, () -> {
            movieService.updateMovie(title, movieRequest);
        });
        assertEquals("Could not find movie with title: " + title, thrown.getMessage());
        verify(movieRepository, times(1)).findByTitle(title);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testUpdateMovie_WhenMovieTitleExists_ShouldThrowMovieAlreadyExistsException() {
        //given
        String currentTitle = "Titanic";
        String newTitle = "Inception";
        String genre = "Drama";
        Integer duration = 194;
        BigDecimal rating = BigDecimal.valueOf(7.9);
        String releaseYear = "1997";
        MovieRequest movieRequest = new MovieRequest(newTitle, genre, duration, rating, releaseYear);
        Movie existingMovie = new Movie(currentTitle, genre, duration, rating, releaseYear);

        when(movieRepository.findByTitle(currentTitle)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.existsByTitle(newTitle)).thenReturn(true);

        //when & then
        MovieAlreadyExistsException thrown = assertThrows(MovieAlreadyExistsException.class, () -> {
            movieService.updateMovie(currentTitle, movieRequest);
        });
        assertEquals("A movie with the title '" + newTitle + "' already exists.", thrown.getMessage());

        verify(movieRepository, times(1)).findByTitle(currentTitle);
        verify(movieRepository, times(1)).existsByTitle(newTitle);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testDeleteMovie_WhenMovieExists_ShouldDeleteMovie() {
        //given
        Long movieId = 1L;
        String title = "Titanic";
        String genre = "Drama";
        Integer duration = 194;
        BigDecimal rating = BigDecimal.valueOf(7.9);
        String releaseYear = "1997";
        Movie movie = new Movie(title, genre, duration, rating, releaseYear);
        movie.setId(movieId);

        when(movieRepository.findByTitle(title)).thenReturn(Optional.of(movie));

        //when
        movieService.deleteMovie(title);

        //then
        verify(movieRepository, times(1)).findByTitle(title);
        verify(movieRepository, times(1)).delete(movie);
    }

    @Test
    void testDeleteMovie_WhenMovieDoesNotExist_ShouldThrowMovieNotFoundException() {
        //given
        String title = "Titanic";

        when(movieRepository.findByTitle(title)).thenReturn(Optional.empty());

        //when & then
        MovieNotFoundException thrown = assertThrows(MovieNotFoundException.class, () -> {
            movieService.deleteMovie(title);
        });
        assertEquals("Could not find movie with title: " + title, thrown.getMessage());
    }
}
