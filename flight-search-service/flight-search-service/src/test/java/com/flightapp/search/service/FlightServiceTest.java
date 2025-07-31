package com.flightapp.search.service;

import com.flightapp.search.dto.FlightResponse;
import com.flightapp.search.model.Flight;
import com.flightapp.search.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {
    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private Flight flight1;
    private Flight flight2;
    private String origin;
    private String destination;
    private String date;

    // Renamed testId to testFlightNumber and changed type to String
    private String testFlightNumber1;
    private String testFlightNumber2;

    @BeforeEach
    void setUp() {
        origin = "DEL";
        destination = "BOM";
        date = "2025-07-20";

        // Initialize flightNumber for each test flight
        testFlightNumber1 = "AI101";
        testFlightNumber2 = "6E202";

        // Updated Flight constructor calls to use String flightNumber as the first argument
        flight1 = new Flight(testFlightNumber1, "DEL", "BOM", date, 150, 5200.0, "10:00 AM");
        flight2 = new Flight(testFlightNumber2, "DEL", "BOM", date, 100, 4800.0, "02:30 PM");
    }

    @Test
    void testSearchFlights_Success() {
        List<Flight> flightsFromRepo = Arrays.asList(flight1, flight2);
        when(flightRepository.findByOriginAndDestinationAndFlightDate(origin, destination, date))
                .thenReturn(flightsFromRepo);

        List<FlightResponse> result = flightService.searchFlights(origin, destination, date);

        assertNotNull(result);
        assertEquals(2, result.size());

        FlightResponse r1 = result.get(0);
        assertEquals(flight1.getFlightNumber(), r1.getFlightNumber());
        assertEquals(flight1.getFare(), r1.getFare());
        assertEquals(flight1.getTimings(), r1.getTimings());

        FlightResponse r2 = result.get(1);
        assertEquals(flight2.getFlightNumber(), r2.getFlightNumber());
        assertEquals(flight2.getFare(), r2.getFare());
        assertEquals(flight2.getTimings(), r2.getTimings());

        verify(flightRepository, times(1)).findByOriginAndDestinationAndFlightDate(origin, destination, date);
    }

    @Test
    void testSearchFlights_NoFlightsFound() {
        when(flightRepository.findByOriginAndDestinationAndFlightDate(origin, destination, date))
                .thenReturn(Collections.emptyList());

        List<FlightResponse> result = flightService.searchFlights(origin, destination, date);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(flightRepository, times(1)).findByOriginAndDestinationAndFlightDate(origin, destination, date);
    }

    @Test
    void testGetAllFlightResponses_Success() {
        List<Flight> flightsFromRepo = Arrays.asList(flight1, flight2);
        when(flightRepository.findAll()).thenReturn(flightsFromRepo);

        List<FlightResponse> result = flightService.getAllFlightResponses();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(flight1.getFlightNumber(), result.get(0).getFlightNumber());
        assertEquals(flight2.getFlightNumber(), result.get(1).getFlightNumber());

        verify(flightRepository, times(1)).findAll();
    }

    @Test
    void testGetAllFlightsRaw_Success() {
        List<Flight> flightsFromRepo = Arrays.asList(flight1, flight2);
        when(flightRepository.findAll()).thenReturn(flightsFromRepo);

        List<Flight> result = flightService.getAllFlightsRaw();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(flight1, result.get(0));
        assertEquals(flight2, result.get(1));

        verify(flightRepository, times(1)).findAll();
    }


    @Test
    void testSaveFlight_Success() {
        when(flightRepository.save(any(Flight.class))).thenReturn(flight1);

        Flight saved = flightService.saveFlight(flight1);

        assertNotNull(saved);
        assertEquals(flight1.getFlightNumber(), saved.getFlightNumber()); // Assert on flightNumber
        verify(flightRepository, times(1)).save(flight1);
    }

    @Test
    void testUpdateFlight_FoundAndUpdated() {
        // Use flightNumber for updates
        Flight updatedFlight = new Flight(testFlightNumber1, "DEL", "BLR", "2025-07-21", 140, 6000.0, "11:15 AM");

        // Mock findById to expect a String flightNumber
        when(flightRepository.findById(testFlightNumber1)).thenReturn(Optional.of(flight1));
        when(flightRepository.save(any(Flight.class))).thenReturn(updatedFlight);

        // Call updateFlight with String flightNumber
        Flight result = flightService.updateFlight(testFlightNumber1, updatedFlight);

        assertNotNull(result);
        assertEquals(updatedFlight.getDestination(), result.getDestination());
        assertEquals(updatedFlight.getFare(), result.getFare());
        assertEquals(updatedFlight.getTimings(), result.getTimings());

        // Verify findById with String flightNumber
        verify(flightRepository, times(1)).findById(testFlightNumber1);
        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void testUpdateFlight_NotFound() {
        String invalidFlightNumber = "NONEXISTENT_FLIGHT"; // Changed to String
        when(flightRepository.findById(invalidFlightNumber)).thenReturn(Optional.empty());

        // Assert that a RuntimeException is thrown with the correct message
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                flightService.updateFlight(invalidFlightNumber, flight1)
        );

        // Updated error message to match the service
        assertEquals("Flight with flightNumber " + invalidFlightNumber + " not found.", ex.getMessage());
        verify(flightRepository, times(1)).findById(invalidFlightNumber);
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void testDeleteFlight_Success() {
        String flightNumToDelete = "AI101"; // Changed to String
        doNothing().when(flightRepository).deleteById(flightNumToDelete);
        flightService.deleteFlight(flightNumToDelete);
        verify(flightRepository, times(1)).deleteById(flightNumToDelete);
    }
}