package com.flightapp.checkin.service;

import com.flightapp.checkin.client.BookingClient;
import com.flightapp.checkin.dto.BookingDTO;
import com.flightapp.checkin.model.Checkin;
import com.flightapp.checkin.repository.CheckinRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckinService {

    private final CheckinRepository checkinRepository;
    private final BookingClient bookingClient;

    public CheckinService(CheckinRepository checkinRepository, BookingClient bookingClient) {
        this.checkinRepository = checkinRepository;
        this.bookingClient = bookingClient;
    }

    public String performCheckin(BookingDTO bookingDTO) {
        // Save check-in info locally
        Checkin existingCheckin = checkinRepository.findByBookingId(bookingDTO.getId());

        if (existingCheckin != null && Boolean.TRUE.equals(existingCheckin.getCheckedIn())) {
            return "Already checked in";
        }

        Checkin checkin = new Checkin();
        checkin.setBookingId(bookingDTO.getId());
        checkin.setCheckedIn(true);
        checkinRepository.save(checkin);

        // Update booking service via Feign client
        bookingDTO.setCheckedIn(true);
        bookingClient.updateCheckinStatus(bookingDTO);

        return "Check-in successful for booking ID " + bookingDTO.getId();
    }
}
