package com.flightapp.checkin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.checkin.client.BookingClient;
import com.flightapp.checkin.dto.BookingDTO;
import com.flightapp.checkin.service.CheckinService;
import com.flightapp.checkin.repository.CheckinRepository;
import com.flightapp.checkin.model.Checkin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CheckinControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CheckinRepository checkinRepository;
    private BookingClient bookingClient;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();

        
        checkinRepository = mock(CheckinRepository.class);
        bookingClient = mock(BookingClient.class);

        
        CheckinService checkinService = new CheckinService(checkinRepository, bookingClient);
        CheckinController controller = new CheckinController(checkinService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testConfirmCheckin_Success() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(123L);
        bookingDTO.setFlightNumber("FL123");
        bookingDTO.setFlightDate("2025-06-03");
        bookingDTO.setPassengerName("John Doe");
        bookingDTO.setCheckedIn(false);

        
        when(checkinRepository.findByBookingId(123L)).thenReturn(null);
        doNothing().when(bookingClient).updateCheckinStatus(any());

        mockMvc.perform(post("/api/checkin/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Check-in successful for booking ID 123"));
    }

    @Test
    void testConfirmCheckin_AlreadyCheckedIn() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(123L);
        bookingDTO.setCheckedIn(true);

        Checkin existingCheckin = new Checkin();
        existingCheckin.setBookingId(123L);
        existingCheckin.setCheckedIn(true);

        when(checkinRepository.findByBookingId(123L)).thenReturn(existingCheckin);

        mockMvc.perform(post("/api/checkin/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Already checked in"));
    }

    @Test
    void testConfirmCheckin_InvalidJson() throws Exception {
        String invalidJson = "{ this is invalid json }";

        mockMvc.perform(post("/api/checkin/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testConfirmCheckin_EmptyBody() throws Exception {
        mockMvc.perform(post("/api/checkin/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }
}