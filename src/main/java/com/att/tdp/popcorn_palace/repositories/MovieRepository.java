package com.att.tdp.popcorn_palace.repositories;

import com.att.tdp.popcorn_palace.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Boolean existsByTitle(String title);
    Optional<Movie> findByTitle(String title);
}
