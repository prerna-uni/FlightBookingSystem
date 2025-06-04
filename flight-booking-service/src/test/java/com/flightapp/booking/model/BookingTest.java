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

    @Test
    void testDefaultConstructorValues() {
        Booking booking = new Booking();

        assertNull(booking.getId());
        assertNull(booking.getFlightNumber());
        assertNull(booking.getFlightDate());
        assertNull(booking.getPassengerName());
        assertEquals(0, booking.getNumberOfSeats()); 
        assertNull(booking.getBookingReference());
        assertFalse(booking.isCheckedIn()); 
        assertNull(booking.getCheckinNumber());
        assertNull(booking.getTotalFare());
    }

    @Test
    void testBooleanCheckedInToggle() {
        Booking booking = new Booking();
        booking.setCheckedIn(false);
        assertFalse(booking.isCheckedIn());
        booking.setCheckedIn(true);
        assertTrue(booking.isCheckedIn());
    }

    @Test
    void testNumberOfSeatsBoundary() {
        Booking booking = new Booking();

        booking.setNumberOfSeats(0);
        assertEquals(0, booking.getNumberOfSeats());

        booking.setNumberOfSeats(-1); 
        assertEquals(-1, booking.getNumberOfSeats());
    }

    @Test
    void testNullAndEmptyStrings() {
        Booking booking = new Booking();

        booking.setFlightNumber(null);
        assertNull(booking.getFlightNumber());

        booking.setFlightNumber("");
        assertEquals("", booking.getFlightNumber());

        booking.setPassengerName(null);
        assertNull(booking.getPassengerName());

        booking.setPassengerName("");
        assertEquals("", booking.getPassengerName());

        booking.setBookingReference(null);
        assertNull(booking.getBookingReference());

        booking.setBookingReference("");
        assertEquals("", booking.getBookingReference());
    }

    @Test
    void testToString() {
        Booking booking = new Booking();
        booking.setId(10L);
        booking.setFlightNumber("AI777");
        booking.setFlightDate("2025-12-31");
        booking.setPassengerName("Jane Doe");
        booking.setNumberOfSeats(1);
        booking.setBookingReference("REF999");
        booking.setCheckedIn(true);
        booking.setCheckinNumber("CHK555");
        booking.setTotalFare(1234.56);

        String toString = booking.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("AI777"));
        assertTrue(toString.contains("2025-12-31"));
        assertTrue(toString.contains("Jane Doe"));
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("REF999"));
        assertTrue(toString.contains("true"));
        assertTrue(toString.contains("CHK555"));
        assertTrue(toString.contains("1234.56"));
        assertTrue(toString.contains("10"));
    }
}
