package com.att.tdp.popcorn_palace.models;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incremented ID
    private Long id;

    // Since we enable update and delete by Movie title, title should be unique and not null
    @Column(nullable = false, unique = true)
    private String title;

    @Column
    private String genre;

    @Column
    private int duration;

    @Column
    private BigDecimal rating;

    @Column
    private String releaseYear;

    // Constructors
    public Movie(String title, String genre, int duration, BigDecimal rating, String releaseYear) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    public Movie() {
    }

    public Long getId() {
        return id;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }
}
