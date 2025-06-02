package com.flightapp.checkin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FlightCheckinServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(FlightCheckinServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FlightCheckinServiceApplication.class, args);
        logger.info("Flight Check-in Service Started Successfully");
    }
}
