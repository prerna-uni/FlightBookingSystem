package com.flightapp.admin.controller;

import com.flightapp.admin.client.FareClient;
import com.flightapp.admin.client.SearchClient;
import com.flightapp.admin.dto.FareDTO;
import com.flightapp.admin.dto.FlightDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private SearchClient searchClient;

	@Autowired
	private FareClient fareClient;

	// FLIGHT ENDPOINTS
	@PostMapping("/flights")
	public ResponseEntity<?> addFlight(@RequestBody FlightDTO flight) {
	    // Step 1: Add flight to search-service
	    ResponseEntity<?> searchResponse = searchClient.addFlight(flight);

	    // Step 2: Add fare to fare-service
	    FareDTO fareDTO = new FareDTO(flight.getFlightNumber(), flight.getFlightDate(), flight.getFare());
	    ResponseEntity<String> fareResponse = fareClient.addFare(fareDTO);

	    // Optional: Return both responses (customize as needed)
	    return ResponseEntity.ok("Flight and fare added successfully.");
	}

	@PutMapping("/flights/{id}")
	public ResponseEntity<?> updateFlight(@PathVariable Long id, @RequestBody FlightDTO flight) {
	    return searchClient.updateFlight(id, flight);
	}

	@DeleteMapping("/flights/{id}")
	public ResponseEntity<?> deleteFlight(@PathVariable Long id) {
	    return searchClient.deleteFlight(id);
	}

	// FARE ENDPOINTS
	@PostMapping("/fares")
	public ResponseEntity<?> addFare(@RequestBody FareDTO fare) {
	    return fareClient.addFare(fare);
	}

	@PutMapping("/fares/{id}")
	public ResponseEntity<?> updateFare(@PathVariable Long id, @RequestBody FareDTO fare) {
	    return fareClient.updateFare(id, fare);
	}

	@DeleteMapping("/fares/{id}")
	public ResponseEntity<?> deleteFare(@PathVariable Long id) {
	    return fareClient.deleteFare(id);
	}

}