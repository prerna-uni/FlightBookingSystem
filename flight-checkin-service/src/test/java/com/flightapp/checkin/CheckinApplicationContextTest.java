package com.flightapp.checkin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.checkin.dto.BookingDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CheckinControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCheckin_Success() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setCheckedIn(false);

        mockMvc.perform(post("/api/checkin/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Check-in successful for booking ID 1"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCheckin_AlreadyCheckedIn() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setCheckedIn(true);

        // First check-in
        mockMvc.perform(post("/api/checkin/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk());

        // Second check-in should show already checked in
        mockMvc.perform(post("/api/checkin/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Already checked in"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCheckin_InvalidJson() throws Exception {
        mockMvc.perform(post("/api/checkin/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid_json"))
                .andExpect(status().isBadRequest()); // Now this should pass instead of 401
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCheckin_NullBody() throws Exception {
        mockMvc.perform(post("/api/checkin/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }
}
