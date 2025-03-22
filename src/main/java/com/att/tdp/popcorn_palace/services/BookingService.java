package com.att.tdp.popcorn_palace.services;

import com.att.tdp.popcorn_palace.dtos.BookingRequest;
import com.att.tdp.popcorn_palace.dtos.BookingResponse;
import com.att.tdp.popcorn_palace.exceptions.BookingAlreadyExistsException;
import com.att.tdp.popcorn_palace.models.Booking;
import com.att.tdp.popcorn_palace.models.Showtime;
import com.att.tdp.popcorn_palace.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingService {
    private final ShowtimeService showtimeService;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(ShowtimeService showtimeService, BookingRepository bookingRepository) {
        this.showtimeService = showtimeService;
        this.bookingRepository = bookingRepository;
    }

    public BookingResponse createBooking(BookingRequest bookingRequest){
        Showtime showtime = showtimeService.getShowtimeEntityById(bookingRequest.getShowtimeId());

        Long showtimeID = showtime.getId();
        Integer seatNumber = bookingRequest.getSeatNumber();
        Optional<Booking> existingBooking = bookingRepository.findByShowtimeAndSeat(showtimeID, seatNumber);

        if (existingBooking.isPresent()) {
            throw new BookingAlreadyExistsException(showtimeID, seatNumber);
        }

        Booking booking = convertBookingRequestToBooking(bookingRequest, showtime);
        bookingRepository.save(booking);
        return convertBookingToBookingResponse(booking);
    }

    private BookingResponse convertBookingToBookingResponse(Booking booking){
        return new BookingResponse(booking.getUuid());
    }

    private Booking convertBookingRequestToBooking(BookingRequest bookingRequest, Showtime showtime){
        return new Booking(
                showtime,
                bookingRequest.getSeatNumber(),
                bookingRequest.getUserId()
        );
    }

}
