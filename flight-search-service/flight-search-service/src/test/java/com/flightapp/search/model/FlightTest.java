package com.flightapp.search.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {
	// Common test data
	private Long id;
	private String origin;
	private String destination;
	private String flightNumber;
	private String flightDate;
	private int seatsAvailable;
	private double fare;
	private String timings;

	@BeforeEach
	void setUp() {
	    id = 1L;
	    origin = "DEL";
	    destination = "BOM";
	    flightNumber = "AI101";
	    flightDate = "2025-07-20";
	    seatsAvailable = 150;
	    fare = 5000.00;
	    timings = "10:00 AM";
	}

	/*
	 * ----------------------------------------------------
	 * Test cases for Constructors
	 * ----------------------------------------------------
	 */
	@Test
	void testNoArgsConstructor() {
	    Flight flight = new Flight();
	    assertNotNull(flight);
	    assertNull(flight.getId());
	    assertNull(flight.getOrigin());
	    assertNull(flight.getDestination());
	    assertNull(flight.getFlightNumber());
	    assertNull(flight.getFlightDate());
	    assertNull(flight.getTimings());
	    assertEquals(0, flight.getSeatsAvailable());
	    assertEquals(0.0, flight.getFare());
	}

	@Test
	void testAllArgsConstructor() {
	    Flight flight = new Flight(
	            id,
	            origin,
	            destination,
	            flightNumber,
	            flightDate,
	            seatsAvailable,
	            fare,
	            timings
	    );

	    assertNotNull(flight);
	    assertEquals(id, flight.getId());
	    assertEquals(origin, flight.getOrigin());
	    assertEquals(destination, flight.getDestination());
	    assertEquals(flightNumber, flight.getFlightNumber());
	    assertEquals(flightDate, flight.getFlightDate());
	    assertEquals(seatsAvailable, flight.getSeatsAvailable());
	    assertEquals(fare, flight.getFare());
	    assertEquals(timings, flight.getTimings());
	}

	/*
	 * ----------------------------------------------------
	 * Test cases for Getters and Setters
	 * ----------------------------------------------------
	 */
	@Test
	void testGettersAndSetters() {
	    Flight flight = new Flight();

	    Long newId = 2L;
	    String newOrigin = "BOM";
	    String newDestination = "BLR";
	    String newFlightNumber = "6E202";
	    String newFlightDate = "2025-08-01";
	    int newSeatsAvailable = 80;
	    double newFare = 6000.50;
	    String newTimings = "2:30 PM";

	    flight.setId(newId);
	    flight.setOrigin(newOrigin);
	    flight.setDestination(newDestination);
	    flight.setFlightNumber(newFlightNumber);
	    flight.setFlightDate(newFlightDate);
	    flight.setSeatsAvailable(newSeatsAvailable);
	    flight.setFare(newFare);
	    flight.setTimings(newTimings);

	    assertEquals(newId, flight.getId());
	    assertEquals(newOrigin, flight.getOrigin());
	    assertEquals(newDestination, flight.getDestination());
	    assertEquals(newFlightNumber, flight.getFlightNumber());
	    assertEquals(newFlightDate, flight.getFlightDate());
	    assertEquals(newSeatsAvailable, flight.getSeatsAvailable());
	    assertEquals(newFare, flight.getFare());
	    assertEquals(newTimings, flight.getTimings());

	    flight.setOrigin(null);
	    assertNull(flight.getOrigin());
	}

	/*
	 * ----------------------------------------------------
	 * Test case for toString()
	 * ----------------------------------------------------
	 */
	@Test
	void testToString() {
	    Flight flight = new Flight(
	            id,
	            origin,
	            destination,
	            flightNumber,
	            flightDate,
	            seatsAvailable,
	            fare,
	            timings
	    );

	    String expectedToString = "Flight{" +
	            "id=" + id +
	            ", origin='" + origin + '\'' +
	            ", destination='" + destination + '\'' +
	            ", flightNumber='" + flightNumber + '\'' +
	            ", flightDate='" + flightDate + '\'' +
	            ", seatsAvailable=" + seatsAvailable +
	            ", fare=" + fare +
	            ", timings='" + timings + '\'' +
	            '}';

	    assertEquals(expectedToString, flight.toString());

	}

	@Test
	void testToStringWithNullsAndDefaults() {
	    Flight flight = new Flight();
	    flight.setId(id);
	    String expectedToString = "Flight{" +
	            "id=" + id +
	            ", origin='null'" +
	            ", destination='null'" +
	            ", flightNumber='null'" +
	            ", flightDate='null'" +
	            ", seatsAvailable=0" +
	            ", fare=0.0" +
	            ", timings='null'" +
	            '}';

	    assertEquals(expectedToString, flight.toString());
	}

}