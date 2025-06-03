package com.flightapp.booking.controller;

import com.flightapp.booking.dto.*;
import com.flightapp.booking.model.Booking;
import com.flightapp.booking.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void shouldCreateBooking() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setFlightNumber("AI101");
        request.setFlightDate("2025-06-15");
        request.setPassengerName("John");
        request.setNumberOfSeats(2);

        BookingResponse response = new BookingResponse();
        response.setBookingReference("REF123");
        response.setStatus("SUCCESS");
        response.setTotalFare(10000.0);
        response.setMessage("Confirmed");

        when(bookingService.createBooking(any())).thenReturn(response);

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.bookingReference").value("REF123"));
    }

    @Test
    void shouldGetBookingByReference() throws Exception {
        Booking booking = new Booking();
        booking.setBookingReference("REF456");

        when(bookingService.getBookingByReference("REF456")).thenReturn(booking);

        mockMvc.perform(get("/api/bookings/REF456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingReference").value("REF456"));
    }

    @Test
    void shouldUpdateCheckinStatus() throws Exception {
        BookingCheckinUpdateRequest request = new BookingCheckinUpdateRequest();
        request.setBookingReference("REF789");
        request.setCheckinNumber("CHK001");

        doNothing().when(bookingService).updateCheckinStatus(any());

        mockMvc.perform(put("/api/bookings/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}

