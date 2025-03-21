package com.att.tdp.popcorn_palace.exceptions;

public class MovieAlreadyExistsException extends RuntimeException {
    public MovieAlreadyExistsException(String title) {
        super("A movie with the title '" + title + "' already exists.");
    }
}