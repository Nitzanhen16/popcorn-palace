package com.att.tdp.popcorn_palace.exceptions;

public class BookingAlreadyExistsException extends RuntimeException {
    public BookingAlreadyExistsException(Long showtimeId, Integer seatNumber) {
        super("A booking with showtime ID '" + showtimeId + "' and seat number '" + seatNumber +"' already exists.");
    }
}