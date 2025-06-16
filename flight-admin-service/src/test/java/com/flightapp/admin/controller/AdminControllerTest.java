package com.flightapp.admin.controller;

import com.flightapp.admin.client.FareClient;
import com.flightapp.admin.client.SearchClient;
import com.flightapp.admin.dto.FareDTO;
import com.flightapp.admin.dto.FlightDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminControllerTest {

    @Mock
    private SearchClient searchClient;

    @Mock
    private FareClient fareClient;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFlight_Success() {
        FlightDTO flightDTO = new FlightDTO("NYC", "LAX", "FL123", "2025-07-01", 150, 250.0);

        when(searchClient.addFlight(any(FlightDTO.class)))
                .thenReturn(new ResponseEntity<>("Flight added to search service", HttpStatus.OK));
        when(fareClient.addFare(any(FareDTO.class)))
                .thenReturn(new ResponseEntity<>("Fare added to fare service", HttpStatus.OK));

        ResponseEntity<?> responseEntity = adminController.addFlight(flightDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Flight and fare added successfully.", responseEntity.getBody());

        verify(searchClient, times(1)).addFlight(flightDTO);
        verify(fareClient, times(1)).addFare(argThat(new ArgumentMatcher<FareDTO>() {
            @Override
            public boolean matches(FareDTO argument) {
                return argument.getFlightNumber().equals("FL123") &&
                       argument.getFlightDate().equals("2025-07-01") &&
                       argument.getFare() == 250.0;
            }
        }));
    }

    @Test
    void addFlight_SearchServiceFails() {
        FlightDTO flightDTO = new FlightDTO("NYC", "LAX", "FL123", "2025-07-01", 150, 250.0);

        // Simulate search service failure
        when(searchClient.addFlight(any(FlightDTO.class)))
                .thenReturn(new ResponseEntity<>("Search service error", HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseEntity<?> responseEntity = adminController.addFlight(flightDTO);

        // Your controller currently returns "Flight and fare added successfully." even if search service fails.
        // This test will reveal that behavior. If you want to return the search service's error,
        // you would need to modify the controller. For 100% coverage, we just need to hit this path.
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode()); // Current behavior
        assertEquals("Flight and fare added successfully.", responseEntity.getBody()); // Current behavior

        verify(searchClient, times(1)).addFlight(flightDTO);
        // Note: fareClient.addFare will still be called in the current controller logic
        // even if searchClient.addFlight returns an error, which might be an issue.
        verify(fareClient, times(1)).addFare(argThat(new ArgumentMatcher<FareDTO>() {
            @Override
            public boolean matches(FareDTO argument) {
                return argument.getFlightNumber().equals("FL123") &&
                       argument.getFlightDate().equals("2025-07-01") &&
                       argument.getFare() == 250.0;
            }
        }));
    }


    @Test
    void addFlight_FareServiceFails() {
        FlightDTO flightDTO = new FlightDTO("NYC", "LAX", "FL123", "2025-07-01", 150, 250.0);

        when(searchClient.addFlight(any(FlightDTO.class)))
                .thenReturn(new ResponseEntity<>("Flight added to search service", HttpStatus.OK));
        // Simulate fare service failure
        when(fareClient.addFare(any(FareDTO.class)))
                .thenReturn(new ResponseEntity<>("Fare service error", HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseEntity<?> responseEntity = adminController.addFlight(flightDTO);

        // Again, your controller returns success regardless of fare service response.
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Flight and fare added successfully.", responseEntity.getBody());

        verify(searchClient, times(1)).addFlight(flightDTO);
        verify(fareClient, times(1)).addFare(argThat(new ArgumentMatcher<FareDTO>() {
            @Override
            public boolean matches(FareDTO argument) {
                return argument.getFlightNumber().equals("FL123") &&
                       argument.getFlightDate().equals("2025-07-01") &&
                       argument.getFare() == 250.0;
            }
        }));
    }


    @Test
    void updateFlight_ClientReturnsNotFound() {
        Long flightId = 1L;
        FlightDTO flightDTO = new FlightDTO("NYC", "LAX", "FL123", "2025-07-01", 100, 300.0);

        when(searchClient.updateFlight(eq(flightId), any(FlightDTO.class)))
                .thenReturn(new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND));

        ResponseEntity<?> responseEntity = adminController.updateFlight(flightId, flightDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Flight not found", responseEntity.getBody());
        verify(searchClient, times(1)).updateFlight(flightId, flightDTO);
    }

    @Test
    void deleteFlight_ClientReturnsForbidden() {
        Long flightId = 1L;

        when(searchClient.deleteFlight(eq(flightId)))
                .thenReturn(new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN));

        ResponseEntity<?> responseEntity = adminController.deleteFlight(flightId);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Forbidden", responseEntity.getBody());
        verify(searchClient, times(1)).deleteFlight(flightId);
    }

    @Test
    void addFare_ClientReturnsBadRequest() {
        FareDTO fareDTO = new FareDTO("FL456", "2025-07-02", 150.0);

        when(fareClient.addFare(any(FareDTO.class)))
                .thenReturn(new ResponseEntity<>("Invalid fare data", HttpStatus.BAD_REQUEST));

        ResponseEntity<?> responseEntity = adminController.addFare(fareDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid fare data", responseEntity.getBody());
        verify(fareClient, times(1)).addFare(fareDTO);
    }

    @Test
    void updateFare_ClientReturnsConflict() {
        Long fareId = 2L;
        FareDTO fareDTO = new FareDTO("FL456", "2025-07-02", 180.0);

        when(fareClient.updateFare(eq(fareId), any(FareDTO.class)))
                .thenReturn(new ResponseEntity<>("Conflict with existing fare", HttpStatus.CONFLICT));

        ResponseEntity<?> responseEntity = adminController.updateFare(fareId, fareDTO);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Conflict with existing fare", responseEntity.getBody());
        verify(fareClient, times(1)).updateFare(fareId, fareDTO);
    }

    @Test
    void deleteFare_ClientReturnsNotFound() {
        Long fareId = 2L;

        when(fareClient.deleteFare(eq(fareId)))
                .thenReturn(new ResponseEntity<>("Fare not found", HttpStatus.NOT_FOUND));

        ResponseEntity<?> responseEntity = adminController.deleteFare(fareId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Fare not found", responseEntity.getBody());
        verify(fareClient, times(1)).deleteFare(fareId);
    }


    // Existing DTO Tests (no changes needed for these unless you find new uncovered lines)

    @Test
    void fareDTOGettersAndSetters() {
        FareDTO fareDTO = new FareDTO();
        fareDTO.setFlightNumber("FN789");
        fareDTO.setFlightDate("2025-08-01");
        fareDTO.setFare(500.0);

        assertEquals("FN789", fareDTO.getFlightNumber());
        assertEquals("2025-08-01", fareDTO.getFlightDate());
        assertEquals(500.0, fareDTO.getFare());
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

        assertEquals("DEL", flightDTO.getOrigin());
        assertEquals("BOM", flightDTO.getDestination());
        assertEquals("IND101", flightDTO.getFlightNumber());
        assertEquals("2025-09-15", flightDTO.getFlightDate());
        assertEquals(200, flightDTO.getSeatsAvailable());
        assertEquals(100.0, flightDTO.getFare());
    }

    @Test
    void fareDTOAllArgsConstructor() {
        FareDTO fareDTO = new FareDTO("FN111", "2025-10-10", 75.50);
        assertEquals("FN111", fareDTO.getFlightNumber());
        assertEquals("2025-10-10", fareDTO.getFlightDate());
        assertEquals(75.50, fareDTO.getFare());
    }

    @Test
    void flightDTOAllArgsConstructor() {
        FlightDTO flightDTO = new FlightDTO("BLR", "HYD", "AIR222", "2025-11-20", 80, 99.99);
        assertEquals("BLR", flightDTO.getOrigin());
        assertEquals("HYD", flightDTO.getDestination());
        assertEquals("AIR222", flightDTO.getFlightNumber());
        assertEquals("2025-11-20", flightDTO.getFlightDate());
        assertEquals(80, flightDTO.getSeatsAvailable());
        assertEquals(99.99, flightDTO.getFare());
    }
}