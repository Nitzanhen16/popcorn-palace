package com.att.tdp.popcorn_palace.exceptions;

public class MovieInvalidInput extends RuntimeException {
    public MovieInvalidInput(String message) {
        super("Invalid movie input: " + message);
    }
}