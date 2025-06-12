package com.flightapp.search.feign;

import com.flightapp.search.dto.FareDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-fare-service", url = "http://localhost:8083")
public interface FareClient {

    @GetMapping("/fares/{flightNumber}/{flightDate}")
    FareDTO getFare(@PathVariable("flightNumber") String flightNumber,
                    @PathVariable("flightDate") String flightDate);
}
