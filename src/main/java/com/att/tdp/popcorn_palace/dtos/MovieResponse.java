package com.att.tdp.popcorn_palace.dtos;

import java.math.BigDecimal;

public class MovieResponse {

    private Long id;
    private String title;
    private String genre;
    private Integer duration; // Assuming the duration is an integer
    private BigDecimal rating; // Assuming the rating is a decimal (BigDecimal)
    private String releaseYear;

    // Constructor
    public MovieResponse(Long id, String title, String genre, Integer duration, BigDecimal rating, String releaseYear) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public String getReleaseYear() { return releaseYear; }
    public void setReleaseYear(String releaseYear) { this.releaseYear = releaseYear; }
}
