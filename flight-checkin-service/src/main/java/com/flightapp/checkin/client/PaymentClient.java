package com.flightapp.checkin.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-payment-service", url = "http://localhost:8086")
public interface PaymentClient {

    @GetMapping("/api/payments/verify/{bookingReference}")
    Boolean isPaymentCompleted(@PathVariable("bookingReference") String bookingReference);
}
