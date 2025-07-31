package com.flightapp.admin.client;

import com.flightapp.admin.dto.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "flight-search-service")
public interface SearchClient {

    @PostMapping("/api/admin/flights")
    ResponseEntity<String> addFlight(@RequestBody FlightDTO flight);

    @PutMapping("/api/admin/flights/{flightNumber}")
    ResponseEntity<String> updateFlight(@PathVariable String flightNumber, @RequestBody FlightDTO flight);

    @DeleteMapping("/api/admin/flights/{flightNumber}")
    ResponseEntity<String> deleteFlight(@PathVariable String flightNumber);

    @GetMapping("/api/admin/flights")
    ResponseEntity<FlightDTO[]> getAllFlights();
}
