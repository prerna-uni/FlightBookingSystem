package com.flightapp.checkin.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String CHECKIN_QUEUE = "checkin-queue";
    public static final String CHECKIN_EXCHANGE = "checkin-exchange";
    public static final String CHECKIN_ROUTING_KEY = "checkin-routing-key";

    @Bean
    public Queue queue() {
        return new Queue(CHECKIN_QUEUE, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(CHECKIN_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(CHECKIN_ROUTING_KEY);
    }
}
