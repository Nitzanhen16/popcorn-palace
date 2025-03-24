package com.att.tdp.popcorn_palace.controllers;

import com.att.tdp.popcorn_palace.contollers.BookingController;
import com.att.tdp.popcorn_palace.dtos.BookingRequest;
import com.att.tdp.popcorn_palace.dtos.BookingResponse;
import com.att.tdp.popcorn_palace.models.Showtime;
import com.att.tdp.popcorn_palace.services.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private BookingService bookingService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddBooking_whenBookingValid_ShouldReturnCreatedBooking() throws Exception {
        //given
        Long showtimeId = 1L;
        Integer seatNumber = 15;

        UUID userId = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        BookingRequest bookingRequest = new BookingRequest(showtimeId, seatNumber, userId);
        BookingResponse bookingResponse = new BookingResponse(bookingId);

        when(bookingService.createBooking(any(BookingRequest.class))).thenReturn(bookingResponse);

        //when
        ResultActions response = mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId").value(bookingResponse.getBookingId().toString()));
    }

    @Test
    void testAddBooking_whenBookingBadParams_ShouldThrowMethodArgumentNotValidException() throws Exception {
        //given
        BookingRequest bookingRequest = new BookingRequest();

        //when
        ResultActions response = mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)));

        //then
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.showtimeId").value("Showtime ID is required"))
                .andExpect(jsonPath("$.userId").value("User ID is required"))
                .andExpect(jsonPath("$.seatNumber").value("Seat number is required"));
    }

}
