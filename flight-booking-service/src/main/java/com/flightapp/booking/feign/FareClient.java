package com.flightapp.booking.feign;

import com.flightapp.booking.dto.FareDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-fare-service", url = "http://localhost:8083/fares")
public interface FareClient {

    @GetMapping("/{flightNumber}/{flightDate}")
    FareDTO getFare(@PathVariable("flightNumber") String flightNumber,
                    @PathVariable("flightDate") String flightDate);
}
