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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminFlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private AdminFlightController adminFlightController;

    private Flight testFlight;
    private Long testFlightId = 1L;

    @BeforeEach
    void setUp() {
        testFlight = new Flight(testFlightId, "DEL", "BOM", "AI101", "2025-07-20", 150, 5000.00);
    }

    @Test
    void testAddFlight_Success() {
        // Mock the service to return the saved flight
        when(flightService.saveFlight(any(Flight.class))).thenReturn(testFlight);

        // Call the controller method
        ResponseEntity<?> responseEntity = adminFlightController.addFlight(testFlight);

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testFlight, responseEntity.getBody());

        // Verify that the service method was called exactly once with the correct argument
        verify(flightService, times(1)).saveFlight(testFlight);
    }

    @Test
    void testUpdateFlight_Success() {
        Flight updatedFlightDetails = new Flight(testFlightId, "DEL", "BLR", "AI101", "2025-07-20", 140, 5500.00);

        // Mock the service to return the updated flight
        when(flightService.updateFlight(eq(testFlightId), any(Flight.class))).thenReturn(updatedFlightDetails);

        // Call the controller method
        ResponseEntity<?> responseEntity = adminFlightController.updateFlight(testFlightId, updatedFlightDetails);

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedFlightDetails, responseEntity.getBody());

        // Verify that the service method was called exactly once with the correct arguments
        verify(flightService, times(1)).updateFlight(eq(testFlightId), eq(updatedFlightDetails));
    }

    @Test
    void testUpdateFlight_NotFoundScenarioHandledByService() {
        // Assume service throws an exception or returns null/Optional.empty() which then causes a 404.
        // In this specific controller, it just passes the service result as ResponseEntity.ok().
        // So, if the service returns null or an error object, the controller will still return 200 OK.
        // A more robust controller would handle nulls or exceptions from the service.
        // For 100% coverage of this controller, we just need to ensure the service method is called.

        when(flightService.updateFlight(anyLong(), any(Flight.class)))
                .thenReturn(null); // Simulate service not finding/updating anything meaningfully

        ResponseEntity<?> responseEntity = adminFlightController.updateFlight(99L, new Flight());

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode()); // Controller still returns OK based on its current logic
        assertNull(responseEntity.getBody()); // Because service returned null

        verify(flightService, times(1)).updateFlight(anyLong(), any(Flight.class));
    }


    @Test
    void testDeleteFlight_Success() {
        // Mock the service call for a void method
        doNothing().when(flightService).deleteFlight(testFlightId);

        // Call the controller method
        ResponseEntity<?> responseEntity = adminFlightController.deleteFlight(testFlightId);

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Flight deleted successfully", responseEntity.getBody());

        // Verify that the service method was called exactly once with the correct argument
        verify(flightService, times(1)).deleteFlight(testFlightId);
    }

}
