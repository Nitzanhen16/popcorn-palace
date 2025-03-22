package com.att.tdp.popcorn_palace.exceptions;

public class ShowtimeNotFoundException extends RuntimeException {
    public ShowtimeNotFoundException(Long id) {
        super("Could not find showtime with id: " + id);
    }
}