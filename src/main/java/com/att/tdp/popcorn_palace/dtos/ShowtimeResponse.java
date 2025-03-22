package com.att.tdp.popcorn_palace.dtos;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShowtimeResponse {

    private Long id;
    private Long movieId; // if you want to include movie reference as id
    private String theater; // the theater name
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;

    // Constructors
    public ShowtimeResponse(Long id, Long movieId, String theater,
                               LocalDateTime startTime, LocalDateTime endTime, BigDecimal price) {
        this.id = id;
        this.movieId = movieId;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public String getTheaterName() { return theater; }
    public void setTheaterName(String theater) { this.theater = theater; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

