package com.att.tdp.popcorn_palace.repositories;

import com.att.tdp.popcorn_palace.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query to find a booking by showtimeId and seatNumber
    @Query("SELECT b FROM Booking b WHERE b.showtime.id = :showtimeId AND b.seatNumber = :seatNumber")
    Optional<Booking> findByShowtimeAndSeat(@Param("showtimeId") Long showtimeId, @Param("seatNumber") Integer seatNumber);
}
