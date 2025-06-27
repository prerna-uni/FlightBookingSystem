package com.flightapp.search.controller;

import com.flightapp.search.model.Flight;
import com.flightapp.search.service.FlightService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/flights")
public class AdminFlightController {
	@Autowired
	private FlightService flightService;

	@PostMapping
	public ResponseEntity<?> addFlight(@RequestBody Flight flight) {
	    return ResponseEntity.ok(flightService.saveFlight(flight));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateFlight(@PathVariable Long id, @RequestBody Flight updatedFlight) {
	    return ResponseEntity.ok(flightService.updateFlight(id, updatedFlight));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteFlight(@PathVariable Long id) {
	    flightService.deleteFlight(id);
	    return ResponseEntity.ok("Flight deleted successfully");
	}
	
	//new
	
	@GetMapping
    public ResponseEntity<List<Flight>> getAllFlightsForAdmin() {
        return ResponseEntity.ok(flightService.getAllFlightsRaw());
	}


}