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
class FlightServiceTest {@Mock
	private FlightRepository flightRepository;

	@InjectMocks
	private FlightService flightService;

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

	    flight1 = new Flight(1L, "DEL", "BOM", "AI101", date, 150, 5200.0, "10:00 AM");
	    flight2 = new Flight(2L, "DEL", "BOM", "6E202", date, 100, 4800.0, "02:30 PM");
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
	void testSaveFlight_Success() {
	    when(flightRepository.save(any(Flight.class))).thenReturn(flight1);

	    Flight saved = flightService.saveFlight(flight1);

	    assertNotNull(saved);
	    assertEquals(flight1.getId(), saved.getId());
	    verify(flightRepository, times(1)).save(flight1);
	}

	@Test
	void testUpdateFlight_FoundAndUpdated() {
	    Flight updatedFlight = new Flight(flight1.getId(), "DEL", "BLR", "AI101", "2025-07-21", 140, 6000.0, "11:15 AM");

	    when(flightRepository.findById(flight1.getId())).thenReturn(Optional.of(flight1));
	    when(flightRepository.save(any(Flight.class))).thenReturn(updatedFlight);

	    Flight result = flightService.updateFlight(flight1.getId(), updatedFlight);

	    assertNotNull(result);
	    assertEquals(updatedFlight.getDestination(), result.getDestination());
	    assertEquals(updatedFlight.getFare(), result.getFare());
	    assertEquals(updatedFlight.getTimings(), result.getTimings());

	    verify(flightRepository, times(1)).findById(flight1.getId());
	    verify(flightRepository, times(1)).save(any(Flight.class));
	}

	@Test
	void testUpdateFlight_NotFound() {
	    Long invalidId = 99L;
	    when(flightRepository.findById(invalidId)).thenReturn(Optional.empty());

	    RuntimeException ex = assertThrows(RuntimeException.class, () ->
	            flightService.updateFlight(invalidId, flight1)
	    );

	    assertEquals("Flight with ID " + invalidId + " not found.", ex.getMessage());
	    verify(flightRepository, times(1)).findById(invalidId);
	    verify(flightRepository, never()).save(any(Flight.class));
	}

	@Test
	void testDeleteFlight_Success() {
	    Long id = 1L;
	    doNothing().when(flightRepository).deleteById(id);
	    flightService.deleteFlight(id);
	    verify(flightRepository, times(1)).deleteById(id);
	}
}