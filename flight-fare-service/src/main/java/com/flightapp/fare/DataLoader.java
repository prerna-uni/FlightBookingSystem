package com.flightapp.fare;

import com.flightapp.fare.model.Fare;
import com.flightapp.fare.repository.FareRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final FareRepository fareRepository;

    public DataLoader(FareRepository fareRepository) {
        this.fareRepository = fareRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        fareRepository.deleteAll();

        // Add some sample fares
        fareRepository.save(new Fare("AI101", "2025-06-01", 3500.0));
        fareRepository.save(new Fare("AI102", "2025-06-01", 4200.0));
        fareRepository.save(new Fare("AI103", "2025-06-02", 3600.0));
    }
}
