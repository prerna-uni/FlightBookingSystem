package com.flightapp.booking.feign;

import com.flightapp.booking.dto.FareDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-search-service", url = "http://localhost:8082" ,contextId = "fareClient")
public interface FareClient {
	@GetMapping("/api/fare/{flightNumber}/{flightDate}")
	FareDTO getFare(@PathVariable("flightNumber") String flightNumber,
	                @PathVariable("flightDate") String flightDate);

}
