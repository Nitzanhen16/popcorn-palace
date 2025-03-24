package com.att.tdp.popcorn_palace.repositories;

import com.att.tdp.popcorn_palace.models.Booking;
import com.att.tdp.popcorn_palace.models.Movie;
import com.att.tdp.popcorn_palace.models.Showtime;
import com.att.tdp.popcorn_palace.models.Theater;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    private Showtime showtime;

    @BeforeEach
    void setup() {
        // need to set up a valid showtime that can be used to test booking
        Movie movie = new Movie("Titanic", "Drama", 195, BigDecimal.valueOf(8.0), "1997");
        movieRepository.save(movie);

        Theater theater = new Theater("Yes Planet");
        theaterRepository.save(theater);

        LocalDateTime start = LocalDateTime.of(2025, 04, 16,20,30);
        LocalDateTime end = LocalDateTime.of(2025, 04, 16,23,30);

        showtime = new Showtime(movie, theater, start, end, BigDecimal.valueOf(45)); // Example showtime
        showtimeRepository.save(showtime);
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
    }

    @Test
    void testFindByShowtimeAndSeat_whenBookingExists_ShouldReturnBooking() {
        //given
        Integer seatNumber = 15;
        UUID userId = UUID.randomUUID();
        UUID uuid = UUID.randomUUID();

        Booking booking = new Booking(showtime, seatNumber, userId);
        booking.setUuid(uuid);

        bookingRepository.save(booking);

        //when
        Optional<Booking> retrievedBooking = bookingRepository.findByShowtimeAndSeat(showtime.getId(), seatNumber);

        //then
        assertThat(retrievedBooking).isPresent();
        assertNotNull(retrievedBooking);
        assertEquals(showtime, retrievedBooking.get().getShowtime());
        assertEquals(uuid, retrievedBooking.get().getUuid());
        assertEquals(seatNumber, retrievedBooking.get().getSeatNumber());
        assertEquals(userId, retrievedBooking.get().getUserId());
    }

    @Test
    void testFindByShowtimeAndSeat_whenBookingDoesNotExists_ShouldReturnEmptyOptional() {
        //given
        Long showtimeId = 1L;
        Integer seatNumber = 15;

        //when
        Optional<Booking> retrievedBooking = bookingRepository.findByShowtimeAndSeat(showtimeId, seatNumber);

        //then
        assertThat(retrievedBooking).isNotPresent();
    }
}
