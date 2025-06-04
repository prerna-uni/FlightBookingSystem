package com.flightapp.search;

import com.flightapp.search.model.Flight;
import com.flightapp.search.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final FlightRepository flightRepository;

    public DataLoader(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Clear old data if any
        flightRepository.deleteAll();

        // Add some sample flights
        flightRepository.save(new Flight(null, "DEL", "MUM", "AI101", "2025-06-01", 50, 3500.0));
        flightRepository.save(new Flight(null, "DEL", "BLR", "AI102", "2025-06-01", 40, 4200.0));
        flightRepository.save(new Flight(null, "MUM", "DEL", "AI103", "2025-06-02", 60, 3600.0));
    }
}