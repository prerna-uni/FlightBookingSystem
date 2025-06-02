package com.flightapp.checkin.service;

import com.flightapp.checkin.client.BookingClient;
import com.flightapp.checkin.dto.BookingDTO;
import com.flightapp.checkin.model.Checkin;
import com.flightapp.checkin.repository.CheckinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CheckinService {

    private static final Logger logger = LoggerFactory.getLogger(CheckinService.class);
    private final CheckinRepository checkinRepository;
    private final BookingClient bookingClient;

    public CheckinService(CheckinRepository checkinRepository, BookingClient bookingClient) {
        this.checkinRepository = checkinRepository;
        this.bookingClient = bookingClient;
    }

    public String performCheckin(BookingDTO bookingDTO) {
        logger.info("=== Entering performCheckin() with booking ID: {} ===", bookingDTO.getId());

        Checkin existingCheckin = checkinRepository.findByBookingId(bookingDTO.getId());
        if (existingCheckin != null && Boolean.TRUE.equals(existingCheckin.getCheckedIn())) {
            logger.warn("Booking ID {} is already checked in.", bookingDTO.getId());
            return "Already checked in";
        }

        Checkin checkin = new Checkin();
        checkin.setBookingId(bookingDTO.getId());
        checkin.setCheckedIn(true);
        checkinRepository.save(checkin);
        logger.info("Check-in record saved for booking ID {}", bookingDTO.getId());

        try {
            bookingDTO.setCheckedIn(true);
            bookingClient.updateCheckinStatus(bookingDTO);
            logger.info("Check-in status updated in Booking Service for booking ID {}", bookingDTO.getId());
        } catch (Exception ex) {
            logger.error("❌ Failed to update booking status via BookingClient: {}", ex.getMessage());
            return "Check-in failed due to Booking Service error";
        }

        return "Check-in successful for booking ID " + bookingDTO.getId();
    }

}
