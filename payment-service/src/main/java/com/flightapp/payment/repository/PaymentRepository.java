package com.flightapp.payment.repository;

import com.flightapp.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByBookingReference(String bookingReference);
}
