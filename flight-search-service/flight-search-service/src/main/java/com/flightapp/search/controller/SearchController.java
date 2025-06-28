package com.flightapp.search.controller;

import com.flightapp.search.dto.FlightResponse;
import com.flightapp.search.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
	private final FlightService flightService;

	@Autowired
	public SearchController(FlightService flightService) {
	    this.flightService = flightService;
	}

	// USER search endpoint
	@GetMapping
	public List<FlightResponse> searchFlights(@RequestParam String origin,
	                                          @RequestParam String destination,
	                                          @RequestParam String date) {
	    return flightService.searchFlights(origin, destination, date);
	}

	// NEW: ADMIN or UI fetch-all endpoint
	@GetMapping("/flights")
	public List<FlightResponse> getAllFlights() {
		return flightService.getAllFlightResponses(); // ✅
	}

}