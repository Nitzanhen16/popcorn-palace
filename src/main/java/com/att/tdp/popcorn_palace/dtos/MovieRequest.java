package com.att.tdp.popcorn_palace.dtos;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class MovieRequest {

    @NotBlank(message = "Movie's title is required")
    private String title;

    @Nullable
    @Size(min = 1,message = "Genre is required")
    private String genre;

    @Nullable
    @Positive(message = "Movie's duration must be a positive integer")
    private Integer duration; // Assuming the duration is an integer

    @Nullable
    @DecimalMin(value = "0.0", inclusive = false, message = "Movie's rating must be greater than 0.0")
    private BigDecimal rating; // Assuming the rating is a decimal (BigDecimal)

    @Nullable
    @Pattern(regexp = "^(19|20)\\d{2}$", message = "Movie's release year is not properly formatted. It must be a four-digit year between 1900 and 2099 (e.g., 1995, 2023).")
    private String releaseYear;

    // Constructor
    public MovieRequest(String title, String genre, Integer duration, BigDecimal rating, String releaseYear) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    // Getters and Setters
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
