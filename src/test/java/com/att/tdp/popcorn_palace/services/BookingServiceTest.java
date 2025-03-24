package com.att.tdp.popcorn_palace.services;

import com.att.tdp.popcorn_palace.dtos.BookingRequest;
import com.att.tdp.popcorn_palace.dtos.BookingResponse;
import com.att.tdp.popcorn_palace.exceptions.BookingAlreadyExistsException;
import com.att.tdp.popcorn_palace.models.Booking;
import com.att.tdp.popcorn_palace.models.Showtime;
import com.att.tdp.popcorn_palace.repositories.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ShowtimeService showtimeService;

    @Test
    void testCreateBooking_whenBookingIsCreatedSuccessfully_ShouldReturnBookingResponse() {
        //given
        Long showtimeId = 1L;
        Integer seatNumber = 15;
        Showtime showtime = new Showtime();
        showtime.setId(showtimeId);

        UUID userId = UUID.randomUUID();
        BookingRequest bookingRequest = new BookingRequest(showtimeId, seatNumber, userId);
        Booking newBooking = new Booking(showtime, seatNumber, userId);

        when(showtimeService.getShowtimeEntityById(showtimeId)).thenReturn(showtime);
        when(bookingRepository.findByShowtimeAndSeat(showtimeId,seatNumber)).thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenReturn(newBooking);

        //when
        BookingResponse bookingResponse = bookingService.createBooking(bookingRequest);

        //then
        assertNotNull(bookingResponse);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCreateBooking_whenBookingExists_ShouldThrowBookingAlreadyExistsException(){
        //given
        Long showtimeId = 1L;
        Integer seatNumber = 15;
        Showtime showtime = new Showtime();
        showtime.setId(showtimeId);

        UUID userId = UUID.randomUUID();
        UUID newUserId = UUID.randomUUID();
        Booking existingBooking = new Booking(showtime, seatNumber, userId);
        BookingRequest newBooking = new BookingRequest(showtimeId, seatNumber, newUserId);

        when(showtimeService.getShowtimeEntityById(showtimeId)).thenReturn(showtime);
        when(bookingRepository.findByShowtimeAndSeat(showtimeId,seatNumber)).thenReturn(Optional.of(existingBooking));

        //when & then
        BookingAlreadyExistsException thrown = assertThrows(BookingAlreadyExistsException.class, () -> bookingService.createBooking(newBooking));

        assertEquals(("A booking with showtime ID '" + showtimeId + "' and seat number '" + seatNumber +"' already exists."), thrown.getMessage());

        verify(bookingRepository, never()).save(any(Booking.class));
    }
}
