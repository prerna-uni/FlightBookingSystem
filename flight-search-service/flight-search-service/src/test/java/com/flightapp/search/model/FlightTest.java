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

    @BeforeEach
    void setUp() {
        // Initialize common test data before each test
        id = 1L;
        origin = "DEL";
        destination = "BOM";
        flightNumber = "AI101";
        flightDate = "2025-07-20";
        seatsAvailable = 150;
        fare = 5000.00;
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
        assertNull(flight.getId()); // ID should be null by default as it's auto-generated
        assertNull(flight.getOrigin());
        assertNull(flight.getDestination());
        assertNull(flight.getFlightNumber());
        assertNull(flight.getFlightDate());
        assertEquals(0, flight.getSeatsAvailable()); // int primitive defaults to 0
        assertEquals(0.0, flight.getFare()); // double primitive defaults to 0.0
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
                fare
        );

        assertNotNull(flight);
        assertEquals(id, flight.getId());
        assertEquals(origin, flight.getOrigin());
        assertEquals(destination, flight.getDestination());
        assertEquals(flightNumber, flight.getFlightNumber());
        assertEquals(flightDate, flight.getFlightDate());
        assertEquals(seatsAvailable, flight.getSeatsAvailable());
        assertEquals(fare, flight.getFare());
    }

    /*
     * ----------------------------------------------------
     * Test cases for Getters and Setters
     * ----------------------------------------------------
     */
    @Test
    void testGettersAndSetters() {
        Flight flight = new Flight(); // Use no-arg constructor to test setters from scratch

        // Test ID
        Long newId = 2L;
        flight.setId(newId);
        assertEquals(newId, flight.getId());

        // Test Origin
        String newOrigin = "BOM";
        flight.setOrigin(newOrigin);
        assertEquals(newOrigin, flight.getOrigin());

        // Test Destination
        String newDestination = "BLR";
        flight.setDestination(newDestination);
        assertEquals(newDestination, flight.getDestination());

        // Test Flight Number
        String newFlightNumber = "6E202";
        flight.setFlightNumber(newFlightNumber);
        assertEquals(newFlightNumber, flight.getFlightNumber());

        // Test Flight Date
        String newFlightDate = "2025-08-01";
        flight.setFlightDate(newFlightDate);
        assertEquals(newFlightDate, flight.getFlightDate());

        // Test Seats Available
        int newSeatsAvailable = 80;
        flight.setSeatsAvailable(newSeatsAvailable);
        assertEquals(newSeatsAvailable, flight.getSeatsAvailable());

        // Test Fare
        double newFare = 6000.50;
        flight.setFare(newFare);
        assertEquals(newFare, flight.getFare());

        // Test setting values back to null/default if applicable (for objects)
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
                fare
        );

        String expectedToString = "Flight [id=" + id +
                                  ", origin=" + origin +
                                  ", destination=" + destination +
                                  ", flightNumber=" + flightNumber +
                                  ", flightDate=" + flightDate +
                                  ", seatsAvailable=" + seatsAvailable +
                                  ", fare=" + fare +
                                  "]";
        assertEquals(expectedToString, flight.toString());
    }

    @Test
    void testToStringWithNullsAndDefaults() {
        // Test toString when some fields are null or default (using no-arg constructor)
        Flight flight = new Flight();
        flight.setId(id); // Set only ID for example

        String expectedToString = "Flight [id=" + id +
                                  ", origin=" + null + // String field set to null
                                  ", destination=" + null +
                                  ", flightNumber=" + null +
                                  ", flightDate=" + null +
                                  ", seatsAvailable=" + 0 + // int primitive default
                                  ", fare=" + 0.0 + // double primitive default
                                  "]";
        assertEquals(expectedToString, flight.toString());
    }
}