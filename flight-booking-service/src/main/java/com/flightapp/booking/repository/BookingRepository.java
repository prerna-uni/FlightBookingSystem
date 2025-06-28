package com.flightapp.booking.repository;

import com.flightapp.booking.model.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findByBookingReference(String bookingReference);
}
