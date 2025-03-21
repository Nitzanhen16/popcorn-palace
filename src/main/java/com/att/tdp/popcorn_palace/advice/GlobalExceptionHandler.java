package com.att.tdp.popcorn_palace.advice;

import com.att.tdp.popcorn_palace.exceptions.MovieNotFoundException;
import com.att.tdp.popcorn_palace.exceptions.MovieAlreadyExistsException;
import com.att.tdp.popcorn_palace.exceptions.InvalidMovieException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle Validations for Requests Body
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return errors;
    }

    // Handle Movie Not Found Exception
    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleMovieNotFound(MovieNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return response;
    }

    // Handle Movie Already Exists Exception
    @ExceptionHandler(MovieAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleMovieAlreadyExists(MovieAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return response;
    }

    // Handle Invalid Movie Exception
    @ExceptionHandler(InvalidMovieException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidMovie(InvalidMovieException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return response;
    }
}