package com.flightapp.checkin.service;

import com.flightapp.checkin.client.BookingClient;
import com.flightapp.checkin.dto.BookingDTO;
import com.flightapp.checkin.model.Checkin;
import com.flightapp.checkin.repository.CheckinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CheckinServiceTest {

    private CheckinRepository checkinRepository;
    private BookingClient bookingClient;
    private CheckinService checkinService;

    @BeforeEach
    void setUp() {
        checkinRepository = mock(CheckinRepository.class);
        bookingClient = mock(BookingClient.class);
        checkinService = new CheckinService(checkinRepository, bookingClient);
    }

    @Test
    void performCheckin_alreadyCheckedIn_returnsAlreadyCheckedInMessage() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);

        Checkin existingCheckin = new Checkin();
        existingCheckin.setBookingId(1L);
        existingCheckin.setCheckedIn(true);

        when(checkinRepository.findByBookingId(1L)).thenReturn(existingCheckin);

        String result = checkinService.performCheckin(bookingDTO);

        assertEquals("Already checked in", result);

        verify(checkinRepository, times(1)).findByBookingId(1L);
        verify(checkinRepository, never()).save(any());
        verifyNoInteractions(bookingClient);
    }

    @Test
    void performCheckin_newCheckin_savesAndUpdatesBooking() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(2L);
        bookingDTO.setCheckedIn(false);

        when(checkinRepository.findByBookingId(2L)).thenReturn(null);

        // We don't care what bookingClient returns (usually void), just verify it is called.
        doNothing().when(bookingClient).updateCheckinStatus(any());

        String result = checkinService.performCheckin(bookingDTO);

        assertEquals("Check-in successful for booking ID 2", result);

        verify(checkinRepository, times(1)).findByBookingId(2L);
        verify(checkinRepository, times(1)).save(any(Checkin.class));
        verify(bookingClient, times(1)).updateCheckinStatus(argThat(dto -> 
            dto.getId().equals(2L) && Boolean.TRUE.equals(dto.getCheckedIn())
        ));
    }

    @Test
    void performCheckin_existingCheckinNotCheckedIn_allowsCheckin() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(3L);
        bookingDTO.setCheckedIn(false);

        Checkin existingCheckin = new Checkin();
        existingCheckin.setBookingId(3L);
        existingCheckin.setCheckedIn(false);

        when(checkinRepository.findByBookingId(3L)).thenReturn(existingCheckin);
        doNothing().when(bookingClient).updateCheckinStatus(any());

        String result = checkinService.performCheckin(bookingDTO);

        assertEquals("Check-in successful for booking ID 3", result);

        verify(checkinRepository, times(1)).save(any(Checkin.class));
        verify(bookingClient, times(1)).updateCheckinStatus(any());
    }
    
    
   
}
