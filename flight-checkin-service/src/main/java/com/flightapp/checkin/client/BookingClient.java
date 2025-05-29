package com.flightapp.checkin.client;

import com.flightapp.checkin.dto.BookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "flight-booking-service", url = "http://localhost:8084")
public interface BookingClient {

    @PutMapping("/api/booking/checkin")
    void updateCheckinStatus(@RequestBody BookingDTO bookingDTO);
}
