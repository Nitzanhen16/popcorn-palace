package com.att.tdp.popcorn_palace.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class BookingRequest {
    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    @NotNull(message = "Seat number is required")
    private Integer seatNumber;

    @NotNull(message = "User ID is required")
    private UUID userId;

    // Getters and Setters
    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
