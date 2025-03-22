package com.att.tdp.popcorn_palace.advice;

import com.att.tdp.popcorn_palace.exceptions.*;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* GLOBAL HANDLERS */

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getCause();
        String errorMessage = "Malformed JSON request";

        if (rootCause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) rootCause;
            if (ife.getTargetType() == LocalDateTime.class) {
                errorMessage = "Invalid date-time format or value: " + ife.getValue() +
                        ". Expected format: 'yyyy-MM-dd'T'HH:mm:ss.SSSSSS'";
            } else {
                String fieldName = ife.getPath().isEmpty() ? "Unknown Field" : ife.getPath().get(0).getFieldName();
                String expectedType = mapToSimpleType(ife.getTargetType());
                String receivedValue = ife.getValue().toString();

                errorMessage = String.format("%s: Expected Type %s, but received %s",
                        fieldName, expectedType, receivedValue);
            }

        } else if (rootCause != null) {
            errorMessage = "Invalid request: " + rootCause.getMessage();
        }

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", errorMessage);

        return errorResponse;
    }

    /* MOVIE HANDLERS */

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
    @ExceptionHandler(MovieInvalidInput.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidMovie(MovieInvalidInput ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return response;
    }

    /* SHOWTIME HANDLERS */

    // Handle Showtime Not Found Exception
    @ExceptionHandler(ShowtimeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleShowtimeNotFound(ShowtimeNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return response;
    }

    @ExceptionHandler(ShowtimeOverlapException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleShowtimeOverlapException(ShowtimeOverlapException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return response;
    }

    private String mapToSimpleType(Class<?> clazz) {
        if (Number.class.isAssignableFrom(clazz)) {
            return "Numeric";  // Integer, Long, Float, Double, BigDecimal
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return "Boolean";
        } else if (clazz == String.class) {
            return "String";
        } else {
            return clazz.getSimpleName();  // Default case
        }
    }
}