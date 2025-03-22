package com.att.tdp.popcorn_palace.repositories;

import com.att.tdp.popcorn_palace.models.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    @Query("SELECT s FROM Showtime s " +
            "WHERE s.theater.id = :theaterId " +
            "AND s.startTime < :endTime " +
            "AND s.endTime > :startTime")
    List<Showtime> findOverlappingShowtimes(
            @Param("theaterId") Long theaterId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
