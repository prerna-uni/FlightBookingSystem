package com.flightapp.admin.controller;

import com.flightapp.admin.client.SearchClient;
import com.flightapp.admin.dto.FlightDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private SearchClient searchClient;

	// FLIGHT ENDPOINTS
	//new
	 @GetMapping("/flights")
	    public ResponseEntity<?> getAllFlights() {
	        return searchClient.getAllFlights();
	    }

	 
	

	@PostMapping("/flights")
	public ResponseEntity<?> addFlight(@RequestBody FlightDTO flight) {
	    // Directly add the flight (including fare) to the search-service
	    return searchClient.addFlight(flight);
	}

	@PutMapping("/flights/{id}")
	public ResponseEntity<?> updateFlight(@PathVariable Long id, @RequestBody FlightDTO flight) {
	    return searchClient.updateFlight(id, flight);
	}

	@DeleteMapping("/flights/{id}")
	public ResponseEntity<?> deleteFlight(@PathVariable Long id) {
	    return searchClient.deleteFlight(id);
	}

}