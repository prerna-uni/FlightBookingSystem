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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository; // Mock the repository

    @Mock
    private FareClient fareClient; // Mock the Feign client

    @InjectMocks
    private FlightService flightService; // Inject mocks into the service under test

    // Common test data
    private Flight flight1;
    private Flight flight2;
    private String origin;
    private String destination;
    private String date;

    @BeforeEach
    void setUp() {
        origin = "DEL";
        destination = "BOM";
        date = "2025-07-20";

        // Initialize sample Flight entities
        flight1 = new Flight(1L, "DEL", "BOM", "AI101", "2025-07-20", 150, 0.0); // Fare initially 0.0 in model
        flight2 = new Flight(2L, "DEL", "BOM", "6E202", "2025-07-20", 100, 0.0);
    }

    /*
     * ----------------------------------------------------
     * Test cases for searchFlights()
     * ----------------------------------------------------
     */
    @Test
    void testSearchFlights_SuccessWithFares() {
        // Mock repository to return flights
        List<Flight> flightsFromRepo = Arrays.asList(flight1, flight2);
        when(flightRepository.findByOriginAndDestinationAndFlightDate(origin, destination, date))
                .thenReturn(flightsFromRepo);

        // Mock FareClient to return specific fares for each flight
        FareDTO fareDTO1 = new FareDTO();
        fareDTO1.setFare(5000.0);
        when(fareClient.getFare(eq("AI101"), eq("2025-07-20"))).thenReturn(fareDTO1);

        FareDTO fareDTO2 = new FareDTO();
        fareDTO2.setFare(4800.0);
        when(fareClient.getFare(eq("6E202"), eq("2025-07-20"))).thenReturn(fareDTO2);

        // Call the service method
        List<FlightResponse> result = flightService.searchFlights(origin, destination, date);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify FlightResponse for flight1
        FlightResponse response1 = result.get(0);
        assertEquals(flight1.getId(), response1.getId());
        assertEquals(flight1.getOrigin(), response1.getOrigin());
        assertEquals(flight1.getDestination(), response1.getDestination());
        assertEquals(flight1.getFlightNumber(), response1.getFlightNumber());
        assertEquals(flight1.getFlightDate(), response1.getFlightDate());
        assertEquals(flight1.getSeatsAvailable(), response1.getSeatsAvailable());
        assertEquals(fareDTO1.getFare(), response1.getFare()); // Check if fare from DTO is set

        // Verify FlightResponse for flight2
        FlightResponse response2 = result.get(1);
        assertEquals(flight2.getId(), response2.getId());
        assertEquals(flight2.getFlightNumber(), response2.getFlightNumber());
        assertEquals(fareDTO2.getFare(), response2.getFare());

        // Verify repository and client calls
        verify(flightRepository, times(1)).findByOriginAndDestinationAndFlightDate(origin, destination, date);
        verify(fareClient, times(1)).getFare(eq("AI101"), eq("2025-07-20"));
        verify(fareClient, times(1)).getFare(eq("6E202"), eq("2025-07-20"));
    }

    @Test
    void testSearchFlights_NoFlightsFound() {
        // Mock repository to return an empty list
        when(flightRepository.findByOriginAndDestinationAndFlightDate(origin, destination, date))
                .thenReturn(Collections.emptyList());

        // Call the service method
        List<FlightResponse> result = flightService.searchFlights(origin, destination, date);

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify repository was called, but FareClient was NOT called
        verify(flightRepository, times(1)).findByOriginAndDestinationAndFlightDate(origin, destination, date);
        verifyNoInteractions(fareClient); // Important: ensure client is not called if no flights
    }

    @Test
    void testSearchFlights_FareClientThrowsException() {
        // Mock repository to return flights
        List<Flight> flightsFromRepo = Collections.singletonList(flight1); // Only one flight for simplicity
        when(flightRepository.findByOriginAndDestinationAndFlightDate(origin, destination, date))
                .thenReturn(flightsFromRepo);

        // Mock FareClient to throw an exception
        when(fareClient.getFare(any(String.class), any(String.class)))
                .thenThrow(new RuntimeException("Fare service unavailable"));

        // Call the service method
        List<FlightResponse> result = flightService.searchFlights(origin, destination, date);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        FlightResponse response = result.get(0);
        assertEquals(flight1.getId(), response.getId());
        // Crucially, fare should be null because the client threw an exception
        assertNull(response.getFare());

        // Verify calls
        verify(flightRepository, times(1)).findByOriginAndDestinationAndFlightDate(origin, destination, date);
        verify(fareClient, times(1)).getFare(eq("AI101"), eq("2025-07-20"));
    }

    @Test
    void testSearchFlights_FareClientReturnsNull() {
        // Mock repository to return flights
        List<Flight> flightsFromRepo = Collections.singletonList(flight1);
        when(flightRepository.findByOriginAndDestinationAndFlightDate(origin, destination, date))
                .thenReturn(flightsFromRepo);

        // Mock FareClient to return null
        when(fareClient.getFare(any(String.class), any(String.class))).thenReturn(null);

        // Call the service method
        List<FlightResponse> result = flightService.searchFlights(origin, destination, date);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        FlightResponse response = result.get(0);
        assertEquals(flight1.getId(), response.getId());
        // Fare should be null because the client returned null
        assertNull(response.getFare());

        // Verify calls
        verify(flightRepository, times(1)).findByOriginAndDestinationAndFlightDate(origin, destination, date);
        verify(fareClient, times(1)).getFare(eq("AI101"), eq("2025-07-20"));
    }

    /*
     * ----------------------------------------------------
     * Test cases for saveFlight()
     * ----------------------------------------------------
     */
    @Test
    void testSaveFlight_Success() {
        // Mock repository to return the saved flight
        when(flightRepository.save(any(Flight.class))).thenReturn(flight1);

        // Call the service method
        Flight savedFlight = flightService.saveFlight(flight1);

        // Assertions
        assertNotNull(savedFlight);
        assertEquals(flight1, savedFlight);

        // Verify repository method was called
        verify(flightRepository, times(1)).save(flight1);
    }

    /*
     * ----------------------------------------------------
     * Test cases for updateFlight()
     * ----------------------------------------------------
     */
    @Test
    void testUpdateFlight_FoundAndUpdated() {
        Flight updatedFlightDetails = new Flight(flight1.getId(), "DEL", "BLR", "AI101", "2025-07-21", 140, 5500.0);

        // Mock repository to find the existing flight
        when(flightRepository.findById(eq(flight1.getId()))).thenReturn(Optional.of(flight1));
        // Mock repository to return the saved (updated) flight
        when(flightRepository.save(any(Flight.class))).thenReturn(updatedFlightDetails);

        // Call the service method
        Flight resultFlight = flightService.updateFlight(flight1.getId(), updatedFlightDetails);

        // Assertions
        assertNotNull(resultFlight);
        assertEquals(updatedFlightDetails.getOrigin(), resultFlight.getOrigin());
        assertEquals(updatedFlightDetails.getDestination(), resultFlight.getDestination());
        assertEquals(updatedFlightDetails.getFlightDate(), resultFlight.getFlightDate());
        assertEquals(updatedFlightDetails.getSeatsAvailable(), resultFlight.getSeatsAvailable());
        assertEquals(updatedFlightDetails.getFare(), resultFlight.getFare());

        // Verify repository methods were called
        verify(flightRepository, times(1)).findById(eq(flight1.getId()));
        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void testUpdateFlight_NotFoundThrowsException() {
        Long nonExistentId = 99L;
        Flight updatedFlightDetails = new Flight(nonExistentId, "DEL", "BLR", "AI101", "2025-07-21", 140, 5500.0);

        // Mock repository to return empty optional (flight not found)
        when(flightRepository.findById(eq(nonExistentId))).thenReturn(Optional.empty());

        // Assert that calling the service method throws RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlight(nonExistentId, updatedFlightDetails);
        });

        // Verify the exception message
        assertEquals("Flight with ID " + nonExistentId + " not found.", exception.getMessage());

        // Verify repository's findById was called, but save was NOT called
        verify(flightRepository, times(1)).findById(eq(nonExistentId));
        verify(flightRepository, never()).save(any(Flight.class));
    }

    /*
     * ----------------------------------------------------
     * Test cases for deleteFlight()
     * ----------------------------------------------------
     */
    @Test
    void testDeleteFlight_Success() {
        Long flightIdToDelete = 1L;

        // Mock the repository's void method
        doNothing().when(flightRepository).deleteById(eq(flightIdToDelete));

        // Call the service method
        flightService.deleteFlight(flightIdToDelete);

        // Verify repository method was called
        verify(flightRepository, times(1)).deleteById(eq(flightIdToDelete));
    }
}