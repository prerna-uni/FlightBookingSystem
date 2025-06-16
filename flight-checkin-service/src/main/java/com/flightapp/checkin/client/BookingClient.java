package com.flightapp.checkin.client;

import com.flightapp.checkin.config.FeignClientInterceptor;
import com.flightapp.checkin.dto.BookingCheckinUpdateRequest;
import com.flightapp.checkin.dto.BookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "flight-booking-service", url = "http://localhost:8084", configuration = FeignClientInterceptor.class)
public interface BookingClient {
    @PutMapping("/api/bookings/checkin")
    void updateCheckinStatus(@RequestBody BookingCheckinUpdateRequest request);
}
