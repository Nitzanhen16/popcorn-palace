package com.att.tdp.popcorn_palace.exceptions;

public class ShowtimeOverlapException extends RuntimeException {
    public ShowtimeOverlapException(String message) {
        super(message);
    }
}