package com.flightapp.booking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void testBookingModelGettersAndSetters() {
        Booking booking = new Booking();

        Long id = 1L;
        String flightNumber = "AI101";
        String flightDate = "2025-06-15";
        String passengerName = "John Doe";
        int numberOfSeats = 2;
        String bookingReference = "REF123";
        boolean checkedIn = true;
        String checkinNumber = "CHK789";
        Double totalFare = 9999.99;

        booking.setId(id);
        booking.setFlightNumber(flightNumber);
        booking.setFlightDate(flightDate);
        booking.setPassengerName(passengerName);
        booking.setNumberOfSeats(numberOfSeats);
        booking.setBookingReference(bookingReference);
        booking.setCheckedIn(checkedIn);
        booking.setCheckinNumber(checkinNumber);
        booking.setTotalFare(totalFare);

        assertEquals(id, booking.getId());
        assertEquals(flightNumber, booking.getFlightNumber());
        assertEquals(flightDate, booking.getFlightDate());
        assertEquals(passengerName, booking.getPassengerName());
        assertEquals(numberOfSeats, booking.getNumberOfSeats());
        assertEquals(bookingReference, booking.getBookingReference());
        assertTrue(booking.isCheckedIn());
        assertEquals(checkinNumber, booking.getCheckinNumber());
        assertEquals(totalFare, booking.getTotalFare());
    }
}
