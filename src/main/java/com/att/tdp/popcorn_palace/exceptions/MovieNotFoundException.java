package com.att.tdp.popcorn_palace.exceptions;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String title) {
        super("Could not find movie with title: " + title);
    }
}