package com.att.tdp.popcorn_palace.exceptions;

public class InvalidMovieException extends RuntimeException {
    public InvalidMovieException(String message) {
        super("Invalid movie input: " + message);
    }
}