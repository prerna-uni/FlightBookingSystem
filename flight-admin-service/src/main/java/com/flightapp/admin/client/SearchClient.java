package com.flightapp.admin.client;

import com.flightapp.admin.dto.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "flight-search-service", configuration = com.flightapp.admin.config.FeignClientConfig.class)
public interface SearchClient {
	@PostMapping("/api/admin/flights")
	ResponseEntity<String> addFlight(@RequestBody FlightDTO flight);

	@PutMapping("/api/admin/flights/{id}")
	ResponseEntity<String> updateFlight(@PathVariable Long id, @RequestBody FlightDTO flight);

	@DeleteMapping("/api/admin/flights/{id}")
	ResponseEntity<String> deleteFlight(@PathVariable Long id);
	
	//new
	 @GetMapping("/api/admin/flights")
	    ResponseEntity<FlightDTO[]> getAllFlights();

}