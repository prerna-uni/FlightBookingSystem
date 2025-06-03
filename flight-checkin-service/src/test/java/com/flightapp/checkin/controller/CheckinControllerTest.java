package com.flightapp.checkin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.checkin.dto.BookingDTO;
import com.flightapp.checkin.service.CheckinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CheckinControllerTest {

    private MockMvc mockMvc;
    private CheckinService checkinService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        checkinService = Mockito.mock(CheckinService.class);
        CheckinController controller = new CheckinController(checkinService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testConfirmCheckin_Success() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(123L);
        bookingDTO.setFlightNumber("FL123");
        bookingDTO.setFlightDate("2025-06-03");
        bookingDTO.setPassengerName("John Doe");
        bookingDTO.setCheckedIn(false);

        Mockito.when(checkinService.performCheckin(any(BookingDTO.class)))
                .thenReturn("Check-in successful for booking ID 123");

        mockMvc.perform(post("/api/checkin/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Check-in successful for booking ID 123"));

        verify(checkinService, times(1)).performCheckin(any(BookingDTO.class));
    }

    @Test
    void testConfirmCheckin_AlreadyCheckedIn() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(123L);
        bookingDTO.setCheckedIn(true);

        Mockito.when(checkinService.performCheckin(any(BookingDTO.class)))
                .thenReturn("Already checked in");

        mockMvc.perform(post("/api/checkin/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Already checked in"));

        verify(checkinService, times(1)).performCheckin(any(BookingDTO.class));
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
        // Since BookingDTO fields are null, service will still be called.
        verify(checkinService, times(1)).performCheckin(any(BookingDTO.class));
    }
}




