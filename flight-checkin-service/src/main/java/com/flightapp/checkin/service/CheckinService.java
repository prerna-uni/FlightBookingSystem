package com.flightapp.checkin.service;

import com.flightapp.checkin.client.BookingClient;
import com.flightapp.checkin.client.PaymentClient;
import com.flightapp.checkin.dto.BookingCheckinUpdateRequest;
import com.flightapp.checkin.dto.BookingDTO;
import com.flightapp.checkin.model.Checkin;
import com.flightapp.checkin.repository.CheckinRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckinService {

    private final CheckinRepository checkinRepository;
    private final BookingClient bookingClient;
    private final PaymentClient paymentClient;

    public CheckinService(CheckinRepository checkinRepository,
                          BookingClient bookingClient,
                          PaymentClient paymentClient) {
        this.checkinRepository = checkinRepository;
        this.bookingClient = bookingClient;
        this.paymentClient = paymentClient;
    }

    public String performCheckin(BookingDTO bookingDTO) {
        Long bookingId = bookingDTO.getId();
        String bookingReference = bookingDTO.getBookingReference();

        // Check if payment is completed using booking reference
        Boolean paymentDone = paymentClient.isPaymentCompleted(bookingReference);
        if (paymentDone == null || !paymentDone) {
            return "Payment not completed for booking reference " + bookingReference + ". Check-in not allowed.";
        }

        // Check if already checked in
        Checkin existingCheckin = checkinRepository.findByBookingId(bookingId);
        if (existingCheckin != null && Boolean.TRUE.equals(existingCheckin.getCheckedIn())) {
            return "Already checked in";
        }

        // Save check-in info locally
        Checkin checkin = new Checkin();
        checkin.setBookingId(bookingId);
        checkin.setCheckedIn(true);
        checkinRepository.save(checkin);

        // Update booking service
       // bookingDTO.setCheckedIn(true);
        BookingCheckinUpdateRequest updateRequest = new BookingCheckinUpdateRequest();
        updateRequest.setBookingReference(bookingDTO.getBookingReference());
        updateRequest.setPassengerName(bookingDTO.getPassengerName());
        updateRequest.setCheckedIn(true);
        bookingClient.updateCheckinStatus(updateRequest);


        return "Check-in successful for booking ID " + bookingId;
    }
}
