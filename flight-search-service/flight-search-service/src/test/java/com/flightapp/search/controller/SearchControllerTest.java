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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchControllerTest {
	@Mock
	private FlightService flightService;

	@InjectMocks
	private SearchController searchController;

	private String origin;
	private String destination;
	private String date;
	private List<FlightResponse> mockFlightResponses;

	@BeforeEach
	void setUp() {
	    origin = "DEL";
	    destination = "BOM";
	    date = "2025-07-20";

	    FlightResponse flight1 = new FlightResponse();
	    flight1.setFlightNumber("AI101");
	    flight1.setOrigin("DEL");
	    flight1.setDestination("BOM");
	    flight1.setFlightDate("2025-07-20");
	    flight1.setSeatsAvailable(100);
	    flight1.setFare(5000.00);
	    flight1.setTimings("10:30 AM");

	    FlightResponse flight2 = new FlightResponse();
	    flight2.setFlightNumber("6E202");
	    flight2.setOrigin("DEL");
	    flight2.setDestination("BOM");
	    flight2.setFlightDate("2025-07-20");
	    flight2.setSeatsAvailable(50);
	    flight2.setFare(4800.00);
	    flight2.setTimings("02:00 PM");

	    mockFlightResponses = Arrays.asList(flight1, flight2);
	}

	@Test
	void testSearchFlights_Success() {
	    when(flightService.searchFlights(eq(origin), eq(destination), eq(date)))
	            .thenReturn(mockFlightResponses);

	    List<FlightResponse> result = searchController.searchFlights(origin, destination, date);

	    assertNotNull(result);
	    assertEquals(2, result.size());
	    assertEquals("AI101", result.get(0).getFlightNumber());
	    assertEquals("10:30 AM", result.get(0).getTimings());
	    assertEquals("6E202", result.get(1).getFlightNumber());
	    assertEquals("02:00 PM", result.get(1).getTimings());

	    verify(flightService, times(1)).searchFlights(eq(origin), eq(destination), eq(date));
	}

	@Test
	void testSearchFlights_NoResults() {
	    when(flightService.searchFlights(eq(origin), eq(destination), eq(date)))
	            .thenReturn(Collections.emptyList());

	    List<FlightResponse> result = searchController.searchFlights(origin, destination, date);

	    assertNotNull(result);
	    assertTrue(result.isEmpty());
	    verify(flightService, times(1)).searchFlights(eq(origin), eq(destination), eq(date));
	}

	@Test
	void testSearchFlights_DifferentParameters() {
	    String newOrigin = "MUM";
	    String newDestination = "CCU";
	    String newDate = "2025-08-01";

	    FlightResponse newFlight = new FlightResponse();
	    newFlight.setFlightNumber("UK505");
	    newFlight.setOrigin("MUM");
	    newFlight.setDestination("CCU");
	    newFlight.setFlightDate("2025-08-01");
	    newFlight.setSeatsAvailable(75);
	    newFlight.setFare(6200.00);
	    newFlight.setTimings("06:45 PM");

	    List<FlightResponse> newMockResponses = Collections.singletonList(newFlight);

	    when(flightService.searchFlights(eq(newOrigin), eq(newDestination), eq(newDate)))
	            .thenReturn(newMockResponses);

	    List<FlightResponse> result = searchController.searchFlights(newOrigin, newDestination, newDate);

	    assertNotNull(result);
	    assertEquals(1, result.size());
	    assertEquals("UK505", result.get(0).getFlightNumber());
	    assertEquals("06:45 PM", result.get(0).getTimings());

	    verify(flightService, times(1)).searchFlights(eq(newOrigin), eq(newDestination), eq(newDate));
	}

}
