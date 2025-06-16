package com.flightapp.payment;

import com.razorpay.RazorpayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.flightapp.payment.client")
public class PaymentServiceApplication {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

    @Bean
    public RazorpayClient razorpayClient() throws Exception {
        return new RazorpayClient(razorpayKey, razorpaySecret);
    }
}
