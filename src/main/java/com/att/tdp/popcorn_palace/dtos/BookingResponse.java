package com.att.tdp.popcorn_palace.dtos;

import java.util.UUID;

public class BookingResponse {
    private UUID bookingId;

    public BookingResponse(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

}
