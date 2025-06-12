package com.flightapp.search.controller;

import com.flightapp.search.dto.FlightResponse;
import com.flightapp.search.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SearchControllerTest {

    private MockMvc mockMvc;
    private FlightService flightServiceMock;

    @BeforeEach
    public void setup() {
        flightServiceMock = Mockito.mock(FlightService.class);
        SearchController searchController = new SearchController(flightServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
    }

    @Test
    public void testSearchFlights_success() throws Exception {
        FlightResponse flightResponse = new FlightResponse();
        flightResponse.setId(1L);
        flightResponse.setOrigin("NYC");
        flightResponse.setDestination("LAX");
        flightResponse.setFlightNumber("AA101");
        flightResponse.setFlightDate("2025-06-03");
        flightResponse.setSeatsAvailable(100);
        flightResponse.setFare(150.0);

        Mockito.when(flightServiceMock.searchFlights(eq("NYC"), eq("LAX"), eq("2025-06-03")))
               .thenReturn(List.of(flightResponse));

        mockMvc.perform(get("/api/search")
                .param("origin", "NYC")
                .param("destination", "LAX")
                .param("date", "2025-06-03"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchFlights_missingOrigin() throws Exception {
        mockMvc.perform(get("/api/search")
                .param("destination", "LAX")
                .param("date", "2025-06-03"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSearchFlights_missingDestination() throws Exception {
        mockMvc.perform(get("/api/search")
                .param("origin", "NYC")
                .param("date", "2025-06-03"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSearchFlights_missingDate() throws Exception {
        mockMvc.perform(get("/api/search")
                .param("origin", "NYC")
                .param("destination", "LAX"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSearchFlights_invalidPath() throws Exception {
        mockMvc.perform(get("/api/searching") // incorrect path
                .param("origin", "NYC")
                .param("destination", "LAX")
                .param("date", "2025-06-03"))
                .andExpect(status().isNotFound());
    }
}
