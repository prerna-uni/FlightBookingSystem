package com.flightapp.search.controller;

import com.flightapp.search.model.Flight;
import com.flightapp.search.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminFlightControllerTest {
    @Mock
    private FlightService flightService;

    @InjectMocks
    private AdminFlightController adminFlightController;

    private Flight testFlight;
    // Changed testFlightId to testFlightNumber and type to String
    private String testFlightNumber;

    @BeforeEach
    void setUp() {
        // Initialize with a flightNumber string
        testFlightNumber = "AI101";
        testFlight = new Flight(testFlightNumber, "DEL", "BOM", "2025-07-20", 150, 5000.00, "10:00 AM");
    }

    @Test
    void testAddFlight_Success() {
        when(flightService.saveFlight(any(Flight.class))).thenReturn(testFlight);

        ResponseEntity<?> responseEntity = adminFlightController.addFlight(testFlight);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testFlight, responseEntity.getBody());

        verify(flightService, times(1)).saveFlight(testFlight);
    }

    @Test
    void testUpdateFlight_Success() {
        // Use flightNumber for updates
        Flight updatedFlightDetails = new Flight(testFlightNumber, "DEL", "BLR", "2025-07-20", 140, 5500.00, "11:30 AM");

        // Mock updateFlight to expect a String flightNumber
        when(flightService.updateFlight(eq(testFlightNumber), any(Flight.class))).thenReturn(updatedFlightDetails);

        ResponseEntity<?> responseEntity = adminFlightController.updateFlight(testFlightNumber, updatedFlightDetails);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedFlightDetails, responseEntity.getBody());

        // Verify updateFlight with a String flightNumber
        verify(flightService, times(1)).updateFlight(eq(testFlightNumber), eq(updatedFlightDetails));
    }

    @Test
    void testUpdateFlight_NotFoundScenarioHandledByService() {
        // Mock updateFlight to expect a String flightNumber
        when(flightService.updateFlight(any(String.class), any(Flight.class))).thenReturn(null);

        ResponseEntity<?> responseEntity = adminFlightController.updateFlight("NONEXISTENT_FLIGHT", new Flight());

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        // Verify updateFlight with a String flightNumber
        verify(flightService, times(1)).updateFlight(any(String.class), any(Flight.class));
    }

    @Test
    void testDeleteFlight_Success() {
        // Mock deleteFlight to expect a String flightNumber
        doNothing().when(flightService).deleteFlight(testFlightNumber);

        ResponseEntity<?> responseEntity = adminFlightController.deleteFlight(testFlightNumber);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Flight deleted successfully", responseEntity.getBody());

        // Verify deleteFlight with a String flightNumber
        verify(flightService, times(1)).deleteFlight(testFlightNumber);
    }

    @Test
    void testGetAllFlightsForAdmin_Success() {
        List<Flight> flights = Arrays.asList(
                testFlight,
                new Flight("UK202", "BOM", "CCU", "2025-07-21", 100, 6000.00, "02:00 PM")
        );

        when(flightService.getAllFlightsRaw()).thenReturn(flights);

        ResponseEntity<List<Flight>> responseEntity = adminFlightController.getAllFlightsForAdmin();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
        assertEquals(flights, responseEntity.getBody());

        verify(flightService, times(1)).getAllFlightsRaw();
    }
}