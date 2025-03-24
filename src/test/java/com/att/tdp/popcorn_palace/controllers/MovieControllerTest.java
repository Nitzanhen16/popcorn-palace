package com.att.tdp.popcorn_palace.controllers;


import com.att.tdp.popcorn_palace.contollers.MovieController;
import com.att.tdp.popcorn_palace.dtos.MovieRequest;
import com.att.tdp.popcorn_palace.dtos.MovieResponse;
import com.att.tdp.popcorn_palace.exceptions.MovieNotFoundException;
import com.att.tdp.popcorn_palace.services.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;


@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllMovies_WhenMoviesExist_ShouldReturnMovieResponseList() throws Exception {
        //given
        Long movieId = 1L;
        String title = "Titanic";
        String genre = "Drama";
        Integer duration = 194;
        BigDecimal rating = BigDecimal.valueOf(7.9);
        String releaseYear = "1997";
        MovieResponse movieResponse = new MovieResponse(movieId, title, genre, duration, rating, releaseYear);
        when(movieService.getAllMovies()).thenReturn(List.of(movieResponse));

        // when
        ResultActions response = mockMvc.perform(get("/movies/all"));

        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].genre").value(genre))
                .andExpect(jsonPath("$[0].duration").value(duration))
                .andExpect(jsonPath("$[0].rating").value(rating))
                .andExpect(jsonPath("$[0].releaseYear").value(releaseYear));

        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    void testAddMovie_whenMovieValid_shouldReturnCreatedMovie() throws Exception {
        //given
        Long movieId = 1L;
        String title = "Titanic";
        String genre = "Drama";
        Integer duration = 194;
        BigDecimal rating = BigDecimal.valueOf(7.9);
        String releaseYear = "1997";
        MovieRequest movieRequest = new MovieRequest(title, genre, duration, rating, releaseYear);
        MovieResponse movieResponse = new MovieResponse(movieId, title, genre, duration, rating, releaseYear);

        when(movieService.createMovie(any(MovieRequest.class))).thenReturn(movieResponse);

        //when
        ResultActions response = mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieRequest)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.genre").value(genre))
                .andExpect(jsonPath("$.duration").value(duration))
                .andExpect(jsonPath("$.rating").value(rating))
                .andExpect(jsonPath("$.releaseYear").value(releaseYear));

        verify(movieService, times(1)).createMovie(any(MovieRequest.class));
    }

    @Test
    void testAddMovie_whenMovieBadParams_shouldThrowMethodArgumentNotValidException() throws Exception {
        //given
        String title = "";
        String genre = "";
        Integer duration = -194;
        BigDecimal rating = BigDecimal.valueOf(-7.9);
        String releaseYear = "a1997";
        MovieRequest movieRequest = new MovieRequest(title, genre, duration, rating, releaseYear);

        //when
        ResultActions response = mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieRequest)));

        //then
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Movie's title is required"))
                .andExpect(jsonPath("$.duration").value("Movie's duration must be a positive integer"))
                .andExpect(jsonPath("$.rating").value("Movie's rating must be greater than 0.0"))
                .andExpect(jsonPath("$.genre").value("Movie's Genre is required"))
                .andExpect(jsonPath("$.releaseYear").value("Movie's release year is not properly formatted. It must be a four-digit year between 1900 and 2099 (e.g., 1995, 2023)."));

        verify(movieService, never()).createMovie(any(MovieRequest.class));
    }

    @Test
    void testUpdateMovie_whenMovieExists_shouldReturnUpdatedMovie() throws Exception {
        //given
        String movieTitle = "Titanic 2";
        MovieRequest movieRequest = new MovieRequest("Titanic", "Drama", 200, BigDecimal.valueOf(8.5), "1997");
        doNothing().when(movieService).updateMovie(eq(movieTitle), any(MovieRequest.class));

        //when
        ResultActions response = mockMvc.perform(post("/movies/update/{movieTitle}", movieTitle)
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieRequest)));

        //then
        response.andExpect(status().isOk());
        verify(movieService, times(1)).updateMovie(eq(movieTitle), any(MovieRequest.class));
    }

    @Test
    void testDeleteMovie_whenMovieExists_shouldDeleteMovie() throws Exception {
        // Given
        String movieTitle = "Titanic";
        doNothing().when(movieService).deleteMovie(eq(movieTitle));

        // When
        ResultActions response = mockMvc.perform(delete("/movies/{movieTitle}", movieTitle));

        // Then
        response.andExpect(status().isOk());

        verify(movieService, times(1)).deleteMovie(eq(movieTitle));
    }

    @Test
    void testDeleteMovie_whenMovieDoesNotExist_shouldThrowMovieNotFoundException() throws Exception {
        // Given
        String movieTitle = "NonExistentMovie";
        doThrow(new MovieNotFoundException(movieTitle)).when(movieService).deleteMovie(eq(movieTitle));

        // When
        ResultActions response = mockMvc.perform(delete("/movies/{movieTitle}", movieTitle));

        // Then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Could not find movie with title: " + movieTitle));
        verify(movieService, times(1)).deleteMovie(eq(movieTitle));
    }
}
