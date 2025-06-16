package com.flightapp.fare.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareTest {

    private Long id;
    private String flightNumber;
    private String flightDate;
    private Double fareAmount;

    @BeforeEach
    void setUp() {
        // Initialize common test data before each test
        id = 1L;
        flightNumber = "FL001";
        flightDate = "2025-10-26";
        fareAmount = 150.75;
    }

    @Test
    void testNoArgsConstructor() {
        // Test the no-argument constructor
        Fare fare = new Fare();
        assertNotNull(fare);
        assertNull(fare.getId()); // ID should be null by default before persisting
        assertNull(fare.getFlightNumber());
        assertNull(fare.getFlightDate());
        assertNull(fare.getFare());
    }

    @Test
    void testAllArgsConstructor() {
        // Test the all-argument constructor (excluding ID, as it's generated)
        Fare fare = new Fare(flightNumber, flightDate, fareAmount);
        assertNotNull(fare);
        assertEquals(flightNumber, fare.getFlightNumber());
        assertEquals(flightDate, fare.getFlightDate());
        assertEquals(fareAmount, fare.getFare());
        assertNull(fare.getId()); // ID should still be null here as it's auto-generated
    }

    @Test
    void testGettersAndSetters() {
        Fare fare = new Fare();

        // Test setId and getId
        fare.setId(id);
        assertEquals(id, fare.getId());

        // Test setFlightNumber and getFlightNumber
        fare.setFlightNumber(flightNumber);
        assertEquals(flightNumber, fare.getFlightNumber());

        // Test setFlightDate and getFlightDate
        fare.setFlightDate(flightDate);
        assertEquals(flightDate, fare.getFlightDate());

        // Test setFare and getFare
        fare.setFare(fareAmount);
        assertEquals(fareAmount, fare.getFare());

        // Test updating values
        String newFlightNumber = "FL002";
        fare.setFlightNumber(newFlightNumber);
        assertEquals(newFlightNumber, fare.getFlightNumber());

        Double newFareAmount = 200.00;
        fare.setFare(newFareAmount);
        assertEquals(newFareAmount, fare.getFare());
    }

    @Test
    void testToString() {
        Fare fare = new Fare(flightNumber, flightDate, fareAmount);
        fare.setId(id); // Set ID for toString verification

        String expectedToString = "Fare{" +
                                  "id=" + id +
                                  ", flightNumber='" + flightNumber + '\'' +
                                  ", flightDate='" + flightDate + '\'' +
                                  ", fare=" + fareAmount +
                                  '}';
        assertEquals(expectedToString, fare.toString());
    }

    @Test
    void testToStringWithNulls() {
        // Test toString when some fields are null
        Fare fare = new Fare(); // Using no-arg constructor to have nulls
        fare.setId(id);
        // flightNumber, flightDate, fareAmount will be null

        String expectedToString = "Fare{" +
                                  "id=" + id +
                                  ", flightNumber='" + null + '\'' + // null is converted to "null" string
                                  ", flightDate='" + null + '\'' +
                                  ", fare=" + null +
                                  '}';
        assertEquals(expectedToString, fare.toString());
    }
}