package com.flightapp.checkin.repository;

import com.flightapp.checkin.model.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {

    Checkin findByBookingId(Long bookingId);
}
