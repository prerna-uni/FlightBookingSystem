package com.flightapp.admin.client;

import com.flightapp.admin.dto.FareDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "flight-fare-service")
public interface FareClient {
	@PostMapping("/admin/fares")
	ResponseEntity<String> addFare(@RequestBody FareDTO fare);

	@PutMapping("/admin/fares/{id}")
	ResponseEntity<String> updateFare(@PathVariable Long id, @RequestBody FareDTO fare);

	@DeleteMapping("/admin/fares/{id}")
	ResponseEntity<String> deleteFare(@PathVariable Long id);

}