package com.flightapp.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FlightBookingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlightBookingServiceApplication.class, args);
    }
}
