package com.flightapp.search.controller;

import com.flightapp.search.model.Flight;
import com.flightapp.search.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/flights")
public class AdminFlightController {
    @Autowired
    private FlightService flightService;

    @PostMapping
    public ResponseEntity<?> addFlight(@RequestBody Flight flight) {
        return ResponseEntity.ok(flightService.saveFlight(flight));
    }

    @PutMapping("/{flightNumber}")
    public ResponseEntity<?> updateFlight(@PathVariable String flightNumber, @RequestBody Flight updatedFlight) {
        return ResponseEntity.ok(flightService.updateFlight(flightNumber, updatedFlight));
    }

    @DeleteMapping("/{flightNumber}")
    public ResponseEntity<?> deleteFlight(@PathVariable String flightNumber) {
        flightService.deleteFlight(flightNumber);
        return ResponseEntity.ok("Flight deleted successfully");
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlightsForAdmin() {
        return ResponseEntity.ok(flightService.getAllFlightsRaw());
    }
}
