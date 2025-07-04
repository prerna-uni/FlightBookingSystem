package com.flightapp.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
@SpringBootApplication
//@EnableDiscoveryClient
@EnableAspectJAutoProxy

@EnableFeignClients(basePackages = "com.flightapp.search.feign")

public class FlightSearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightSearchServiceApplication.class, args);
	}
	

}
