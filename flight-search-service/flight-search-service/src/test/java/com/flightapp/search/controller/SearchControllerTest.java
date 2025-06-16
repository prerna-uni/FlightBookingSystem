package com.flightapp.search.controller;

import com.flightapp.search.dto.FlightResponse;
import com.flightapp.search.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchControllerTest {

    @Mock
    private FlightService flightService; // Mock the dependency

    @InjectMocks
    private SearchController searchController; // Inject mock into the controller under test

    private String origin;
    private String destination;
    private String date;
    private List<FlightResponse> mockFlightResponses;

    @BeforeEach
    void setUp() {
        origin = "DEL";
        destination = "BOM";
        date = "2025-07-20";

        // Prepare some mock flight responses using setters:
        FlightResponse flight1 = new FlightResponse();
        flight1.setFlightNumber("AI101");
        flight1.setOrigin("DEL");
        flight1.setDestination("BOM");
        flight1.setFlightDate("2025-07-20");
        flight1.setSeatsAvailable(100);
        flight1.setFare(5000.00);

        FlightResponse flight2 = new FlightResponse();
        flight2.setFlightNumber("6E202");
        flight2.setOrigin("DEL");
        flight2.setDestination("BOM");
        flight2.setFlightDate("2025-07-20");
        flight2.setSeatsAvailable(50);
        flight2.setFare(4800.00);

        mockFlightResponses = Arrays.asList(flight1, flight2);
    }

    // ... rest of your test methods remain the same ...

    @Test
    void testSearchFlights_DifferentParameters() {
        String newOrigin = "MUM";
        String newDestination = "CCU";
        String newDate = "2025-08-01";

        // Prepare new flight response using setters
        FlightResponse newFlight = new FlightResponse();
        newFlight.setFlightNumber("UK505");
        newFlight.setOrigin("MUM");
        newFlight.setDestination("CCU");
        newFlight.setFlightDate("2025-08-01");
        newFlight.setSeatsAvailable(75);
        newFlight.setFare(6200.00);

        List<FlightResponse> newMockResponses = Collections.singletonList(newFlight);

        when(flightService.searchFlights(eq(newOrigin), eq(newDestination), eq(newDate)))
                .thenReturn(newMockResponses);

        List<FlightResponse> result = searchController.searchFlights(newOrigin, newDestination, newDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(newMockResponses, result);

        verify(flightService, times(1)).searchFlights(eq(newOrigin), eq(newDestination), eq(newDate));
    }
}