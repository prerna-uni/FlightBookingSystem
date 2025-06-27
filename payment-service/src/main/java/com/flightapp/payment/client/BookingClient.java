package com.flightapp.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "flight-booking-service", url = "http://localhost:8084")
public interface BookingClient {

	@PutMapping("/api/bookings/payment-confirmed/{bookingReference}")
    String updatePaymentStatus(@PathVariable("bookingReference") String bookingReference);
}