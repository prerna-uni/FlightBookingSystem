package com.flightapp.admin.controller;

import com.flightapp.admin.client.SearchClient;
import com.flightapp.admin.dto.FlightDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private SearchClient searchClient;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllFlights_Success() {
        FlightDTO[] flights = {
            new FlightDTO("DEL", "BOM", "FL001", "2025-07-01", 150, 299.99, "10:30 AM"),
            new FlightDTO("BLR", "HYD", "FL002", "2025-07-02", 120, 199.99, "5:00 PM")
        };

        when(searchClient.getAllFlights())
                .thenReturn(new ResponseEntity<>(flights, HttpStatus.OK));

        ResponseEntity<?> response = adminController.getAllFlights();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
        verify(searchClient, times(1)).getAllFlights();
    }

    @Test
    void addFlight_Success() {
        FlightDTO flightDTO = new FlightDTO("NYC", "LAX", "FL123", "2025-07-01", 150, 250.0, "10:00 AM");

        when(searchClient.addFlight(any(FlightDTO.class)))
                .thenReturn(new ResponseEntity<>("Flight added to search service", HttpStatus.OK));

        ResponseEntity<?> responseEntity = adminController.addFlight(flightDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Flight added to search service", responseEntity.getBody());

        verify(searchClient, times(1)).addFlight(flightDTO);
    }

    @Test
    void addFlight_SearchServiceFails() {
        FlightDTO flightDTO = new FlightDTO("NYC", "LAX", "FL123", "2025-07-01", 150, 250.0, "10:00 AM");

        when(searchClient.addFlight(any(FlightDTO.class)))
                .thenReturn(new ResponseEntity<>("Search service error", HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseEntity<?> responseEntity = adminController.addFlight(flightDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Search service error", responseEntity.getBody());

        verify(searchClient, times(1)).addFlight(flightDTO);
    }

    @Test
    void updateFlight_ClientReturnsNotFound() {
        String flightNumber = "FL123";
        FlightDTO flightDTO = new FlightDTO("NYC", "LAX", flightNumber, "2025-07-01", 100, 300.0, "8:00 AM");

        when(searchClient.updateFlight(eq(flightNumber), any(FlightDTO.class)))
                .thenReturn(new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND));

        ResponseEntity<?> responseEntity = adminController.updateFlight(flightNumber, flightDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Flight not found", responseEntity.getBody());
        verify(searchClient, times(1)).updateFlight(flightNumber, flightDTO);
    }

    @Test
    void deleteFlight_ClientReturnsForbidden() {
        String flightNumber = "FL123";

        when(searchClient.deleteFlight(eq(flightNumber)))
                .thenReturn(new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN));

        ResponseEntity<?> responseEntity = adminController.deleteFlight(flightNumber);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Forbidden", responseEntity.getBody());
        verify(searchClient, times(1)).deleteFlight(flightNumber);
    }

    @Test
    void flightDTOGettersAndSetters() {
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setOrigin("DEL");
        flightDTO.setDestination("BOM");
        flightDTO.setFlightNumber("IND101");
        flightDTO.setFlightDate("2025-09-15");
        flightDTO.setSeatsAvailable(200);
        flightDTO.setFare(100.0);
        flightDTO.setTimings("6:00 PM");

        assertEquals("DEL", flightDTO.getOrigin());
        assertEquals("BOM", flightDTO.getDestination());
        assertEquals("IND101", flightDTO.getFlightNumber());
        assertEquals("2025-09-15", flightDTO.getFlightDate());
        assertEquals(200, flightDTO.getSeatsAvailable());
        assertEquals(100.0, flightDTO.getFare());
        assertEquals("6:00 PM", flightDTO.getTimings());
    }

    @Test
    void flightDTOAllArgsConstructor() {
        FlightDTO flightDTO = new FlightDTO("BLR", "HYD", "AIR222", "2025-11-20", 80, 99.99, "9:45 AM");

        assertEquals("BLR", flightDTO.getOrigin());
        assertEquals("HYD", flightDTO.getDestination());
        assertEquals("AIR222", flightDTO.getFlightNumber());
        assertEquals("2025-11-20", flightDTO.getFlightDate());
        assertEquals(80, flightDTO.getSeatsAvailable());
        assertEquals(99.99, flightDTO.getFare());
        assertEquals("9:45 AM", flightDTO.getTimings());
    }
}