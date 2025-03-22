package com.att.tdp.popcorn_palace.contollers;

import com.att.tdp.popcorn_palace.dtos.BookingRequest;
import com.att.tdp.popcorn_palace.dtos.BookingResponse;
import com.att.tdp.popcorn_palace.services.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponse addBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        return bookingService.createBooking(bookingRequest);
    }
}
