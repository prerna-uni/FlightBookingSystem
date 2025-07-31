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

    // Get all flights
    @GetMapping("/flights")
    public ResponseEntity<?> getAllFlights() {
        return searchClient.getAllFlights();
    }

    // Add new flight
    @PostMapping("/flights")
    public ResponseEntity<?> addFlight(@RequestBody FlightDTO flight) {
        return searchClient.addFlight(flight);
    }

    // Update flight by flightNumber (was ID before)
    @PutMapping("/flights/{flightNumber}")
    public ResponseEntity<?> updateFlight(@PathVariable String flightNumber, @RequestBody FlightDTO flight) {
        return searchClient.updateFlight(flightNumber, flight);
    }

    // Delete flight by flightNumber (was ID before)
    @DeleteMapping("/flights/{flightNumber}")
    public ResponseEntity<?> deleteFlight(@PathVariable String flightNumber) {
        return searchClient.deleteFlight(flightNumber);
    }
}
