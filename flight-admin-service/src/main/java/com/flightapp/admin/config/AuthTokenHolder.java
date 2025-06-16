package com.flightapp.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AuthTokenHolder {
	@Value("${auth.service.url}")
	private String authUrl;

	@Value("${auth.admin.username}")
	private String username;

	@Value("${auth.admin.password}")
	private String password;

	private String token;

	public String getToken() {
	    return token;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void fetchTokenOnStartup() {
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    String body = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
	    HttpEntity<String> request = new HttpEntity<>(body, headers);

	    try {
	        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(authUrl, request, TokenResponse.class);
	        if (response.getStatusCode() == HttpStatus.OK) {
	            this.token = "Bearer " + response.getBody().getToken();
	            System.out.println("Admin-service fetched token: " + token);
	        } else {
	            System.err.println("Failed to authenticate admin.");
	        }
	    } catch (Exception e) {
	        System.err.println("Token fetch error: " + e.getMessage());
	    }
	}

	public static class TokenResponse {
	    private String token;

	    public String getToken() { return token; }
	    public void setToken(String token) { this.token = token; }
	}

}