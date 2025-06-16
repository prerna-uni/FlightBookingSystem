package com.flightapp.fare.controller;

import com.flightapp.fare.model.Fare;
import com.flightapp.fare.repository.FareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin/fares")
@CrossOrigin(origins = "*")
public class AdminFareController { 
	@Autowired
	private FareRepository fareRepository;

	@PostMapping
	public ResponseEntity<?> addFare(@RequestBody Fare fare) {
	    Fare savedFare = fareRepository.save(fare);
	    return ResponseEntity.ok(savedFare);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateFare(@PathVariable Long id, @RequestBody Fare updatedFare) {
	    Optional<Fare> optionalFare = fareRepository.findById(id);
	    if (optionalFare.isPresent()) {
	        Fare existing = optionalFare.get();
	        existing.setFlightNumber(updatedFare.getFlightNumber());
	        existing.setFlightDate(updatedFare.getFlightDate());
	        existing.setFare(updatedFare.getFare());
	        fareRepository.save(existing);
	        return ResponseEntity.ok(existing);
	    } else {
	        return ResponseEntity.status(404).body("Fare not found with id: " + id);
	    }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteFare(@PathVariable Long id) {
	    if (fareRepository.existsById(id)) {
	        fareRepository.deleteById(id);
	        return ResponseEntity.ok("Fare deleted successfully");
	    } else {
	        return ResponseEntity.status(404).body("Fare not found with id: " + id);
	    }
	}

}