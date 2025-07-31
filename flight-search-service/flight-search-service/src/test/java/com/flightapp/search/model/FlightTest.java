package com.flightapp.search.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {
    // Common test data
    // Removed 'id' as it's no longer in your Flight model
    private String origin;
    private String destination;
    private String flightNumber; // Primary key now
    private String flightDate;
    private int seatsAvailable;
    private double fare;
    private String timings;

    @BeforeEach
    void setUp() {
        // Removed id initialization
        origin = "DEL";
        destination = "BOM";
        flightNumber = "AI101"; // Used as the primary key
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
        // Removed assertion for id
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
        // Updated constructor call to match the current Flight model
        Flight flight = new Flight(
                flightNumber, // Primary key is now the first argument
                origin,
                destination,
                flightDate,
                seatsAvailable,
                fare,
                timings
        );

        assertNotNull(flight);
        // Removed assertion for id
        assertEquals(flightNumber, flight.getFlightNumber()); // Assert flightNumber as primary key
        assertEquals(origin, flight.getOrigin());
        assertEquals(destination, flight.getDestination());
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

        // Removed newId
        String newOrigin = "BOM";
        String newDestination = "BLR";
        String newFlightNumber = "6E202"; // Updated to String
        String newFlightDate = "2025-08-01";
        int newSeatsAvailable = 80;
        double newFare = 6000.50;
        String newTimings = "2:30 PM";

        // Removed setId
        flight.setFlightNumber(newFlightNumber); // Setting the flightNumber
        flight.setOrigin(newOrigin);
        flight.setDestination(newDestination);
        flight.setFlightDate(newFlightDate);
        flight.setSeatsAvailable(newSeatsAvailable);
        flight.setFare(newFare);
        flight.setTimings(newTimings);

        // Removed assertion for id
        assertEquals(newFlightNumber, flight.getFlightNumber()); // Asserting flightNumber
        assertEquals(newOrigin, flight.getOrigin());
        assertEquals(newDestination, flight.getDestination());
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
        // Updated constructor call to match the current Flight model
        Flight flight = new Flight(
                flightNumber, // Primary key
                origin,
                destination,
                flightDate,
                seatsAvailable,
                fare,
                timings
        );

        String expectedToString = "Flight{" +
                "flightNumber='" + flightNumber + '\'' + // Changed id to flightNumber
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
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
        // Set only the flightNumber, as it's the primary key and cannot be null if persisted
        flight.setFlightNumber(flightNumber);

        String expectedToString = "Flight{" +
                "flightNumber='" + flightNumber + '\'' + // Changed id to flightNumber
                ", origin='null'" +
                ", destination='null'" +
                ", flightDate='null'" +
                ", seatsAvailable=0" +
                ", fare=0.0" +
                ", timings='null'" +
                '}';

        assertEquals(expectedToString, flight.toString());
    }
}