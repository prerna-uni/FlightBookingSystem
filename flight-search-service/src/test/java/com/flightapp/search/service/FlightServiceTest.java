package com.flightapp.search.service;

import com.flightapp.search.dto.FareDTO;
import com.flightapp.search.dto.FlightResponse;
import com.flightapp.search.feign.FareClient;
import com.flightapp.search.model.Flight;
import com.flightapp.search.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FareClient fareClient;

    @InjectMocks
    private FlightService flightService;

    private Flight sampleFlight;

    @BeforeEach
    void setUp() {
        sampleFlight = new Flight();
        sampleFlight.setId(1L);
        sampleFlight.setOrigin("DEL");
        sampleFlight.setDestination("BLR");
        sampleFlight.setFlightNumber("AI101");
        sampleFlight.setFlightDate("2025-06-05");
        sampleFlight.setSeatsAvailable(50);
        sampleFlight.setFare(300.0);
    }

    @Test
    void testSearchFlights_withFareFromClient() {
        when(flightRepository.findByOriginAndDestinationAndFlightDate("DEL", "BLR", "2025-06-05"))
                .thenReturn(Collections.singletonList(sampleFlight));

        FareDTO fareDTO = new FareDTO();
        fareDTO.setFare(350.0);
        when(fareClient.getFare("AI101", "2025-06-05")).thenReturn(fareDTO);

        List<FlightResponse> results = flightService.searchFlights("DEL", "BLR", "2025-06-05");

        assertEquals(1, results.size());
        FlightResponse response = results.get(0);
        assertEquals("DEL", response.getOrigin());
        assertEquals("BLR", response.getDestination());
        assertEquals("AI101", response.getFlightNumber());
        assertEquals("2025-06-05", response.getFlightDate());
        assertEquals(50, response.getSeatsAvailable());
        assertEquals(300.0, response.getFare()); 
    }

    @Test
    void testSearchFlights_whenFareClientThrowsException() {
        when(flightRepository.findByOriginAndDestinationAndFlightDate("DEL", "BLR", "2025-06-05"))
                .thenReturn(Collections.singletonList(sampleFlight));

        when(fareClient.getFare("AI101", "2025-06-05")).thenThrow(new RuntimeException("Service down"));

        List<FlightResponse> results = flightService.searchFlights("DEL", "BLR", "2025-06-05");

        assertEquals(1, results.size());
        FlightResponse response = results.get(0);
        assertEquals("DEL", response.getOrigin());
        assertEquals("BLR", response.getDestination());
        assertEquals("AI101", response.getFlightNumber());
        assertEquals("2025-06-05", response.getFlightDate());
        assertEquals(50, response.getSeatsAvailable());
        assertEquals(300.0, response.getFare()); // fallback to flight.getFare()
    }

    @Test
    void testSearchFlights_returnsEmptyListWhenNoFlights() {
        when(flightRepository.findByOriginAndDestinationAndFlightDate("DEL", "BLR", "2025-06-05"))
                .thenReturn(Collections.emptyList());

        List<FlightResponse> results = flightService.searchFlights("DEL", "BLR", "2025-06-05");

        assertTrue(results.isEmpty());
        verify(flightRepository, times(1)).findByOriginAndDestinationAndFlightDate("DEL", "BLR", "2025-06-05");
        verifyNoInteractions(fareClient); 
    }
    
    @Test
    void testSearchFlights_whenFareClientReturnsNull() {
        when(flightRepository.findByOriginAndDestinationAndFlightDate("DEL", "BLR", "2025-06-05"))
                .thenReturn(Collections.singletonList(sampleFlight));

        when(fareClient.getFare("AI101", "2025-06-05")).thenReturn(null); // return null FareDTO

        List<FlightResponse> results = flightService.searchFlights("DEL", "BLR", "2025-06-05");

        assertEquals(1, results.size());
        FlightResponse response = results.get(0);
        assertEquals("DEL", response.getOrigin());
        assertEquals("BLR", response.getDestination());
        assertEquals("AI101", response.getFlightNumber());
        assertEquals("2025-06-05", response.getFlightDate());
        assertEquals(50, response.getSeatsAvailable());
        assertEquals(300.0, response.getFare()); // still fallback to flight.getFare()
    }

}
