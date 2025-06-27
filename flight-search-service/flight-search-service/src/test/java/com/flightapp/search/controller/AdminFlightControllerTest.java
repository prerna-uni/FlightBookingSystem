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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
	    testFlight = new Flight(testFlightId, "DEL", "BOM", "AI101", "2025-07-20", 150, 5000.00, "10:00 AM");
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
	    Flight updatedFlightDetails = new Flight(testFlightId, "DEL", "BLR", "AI101", "2025-07-20", 140, 5500.00, "11:30 AM");

	    when(flightService.updateFlight(eq(testFlightId), any(Flight.class))).thenReturn(updatedFlightDetails);

	    ResponseEntity<?> responseEntity = adminFlightController.updateFlight(testFlightId, updatedFlightDetails);

	    assertNotNull(responseEntity);
	    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	    assertEquals(updatedFlightDetails, responseEntity.getBody());

	    verify(flightService, times(1)).updateFlight(eq(testFlightId), eq(updatedFlightDetails));
	}

	@Test
	void testUpdateFlight_NotFoundScenarioHandledByService() {
	    when(flightService.updateFlight(anyLong(), any(Flight.class))).thenReturn(null);

	    ResponseEntity<?> responseEntity = adminFlightController.updateFlight(99L, new Flight());

	    assertNotNull(responseEntity);
	    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	    assertNull(responseEntity.getBody());

	    verify(flightService, times(1)).updateFlight(anyLong(), any(Flight.class));
	}

	@Test
	void testDeleteFlight_Success() {
	    doNothing().when(flightService).deleteFlight(testFlightId);

	    ResponseEntity<?> responseEntity = adminFlightController.deleteFlight(testFlightId);

	    assertNotNull(responseEntity);
	    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	    assertEquals("Flight deleted successfully", responseEntity.getBody());

	    verify(flightService, times(1)).deleteFlight(testFlightId);
	}

}