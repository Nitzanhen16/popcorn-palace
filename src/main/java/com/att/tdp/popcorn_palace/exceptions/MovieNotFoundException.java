package com.att.tdp.popcorn_palace.exceptions;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Long id) {
        super("Could not find movie with ID " + id);
    }

    public MovieNotFoundException(String title) {
        super("Could not find movie with title: " + title);
    }
}