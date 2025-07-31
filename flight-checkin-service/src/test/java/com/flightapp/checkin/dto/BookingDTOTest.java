package com.flightapp.checkin.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookingDTOTest {

    @Test
    void testNoArgsConstructor() {
        // This implicitly calls the no-arg constructor
        BookingDTO dto = new BookingDTO();
        assertNotNull(dto);
        // Default values for fields should be null or false for Boolean
        assertNull(dto.getId());
        assertNull(dto.getBookingReference());
        assertNull(dto.getFlightNumber());
        assertNull(dto.getFlightDate());
        assertNull(dto.getPassengerName());
        assertNull(dto.getCheckedIn()); // Boolean defaults to null
    }

    @Test
    void testGettersAndSetters() {
        BookingDTO dto = new BookingDTO();

        // Sample data
        Long id = 123L;
        String bookingReference = "BOOKREF987";
        String flightNumber = "FLIGHT007";
        String flightDate = "2025-09-10";
        String passengerName = "Jane Doe";
        Boolean checkedIn = true;

        // Set values using setters
        dto.setId(id);
        dto.setBookingReference(bookingReference);
        dto.setFlightNumber(flightNumber);
        dto.setFlightDate(flightDate);
        dto.setPassengerName(passengerName);
        dto.setCheckedIn(checkedIn);

        // Verify values using getters
        assertEquals(id, dto.getId());
        assertEquals(bookingReference, dto.getBookingReference());
        assertEquals(flightNumber, dto.getFlightNumber());
        assertEquals(flightDate, dto.getFlightDate());
        assertEquals(passengerName, dto.getPassengerName());
        assertEquals(checkedIn, dto.getCheckedIn());
    }

    @Test
    void testToStringMethod() {
        BookingDTO dto = new BookingDTO();
        dto.setId(456L);
        dto.setBookingReference("XYZABC");
        dto.setFlightNumber("AIR123");
        dto.setFlightDate("2025-07-20");
        dto.setPassengerName("John Smith");
        dto.setCheckedIn(false);

        String expectedToString = "BookingDTO{" +
                "id=456, bookingReference='XYZABC', flightNumber='AIR123', flightDate='2025-07-20', " +
                "passengerName='John Smith', checkedIn=false}";

        assertEquals(expectedToString, dto.toString());
    }
}