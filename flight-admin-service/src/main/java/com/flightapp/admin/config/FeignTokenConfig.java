package com.flightapp.admin.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignTokenConfig {
	private final AuthTokenHolder tokenHolder;

	public FeignTokenConfig(AuthTokenHolder tokenHolder) {
	    this.tokenHolder = tokenHolder;
	}

	@Bean
	public RequestInterceptor requestInterceptor() {
	    return template -> {
	        String token = tokenHolder.getToken();
	        if (token != null) {
	            template.header("Authorization", token);
	        }
	    };
	}

}