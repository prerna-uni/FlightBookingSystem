package com.flightapp.admin.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
	@Value("${spring.security.oauth2.resourceserver.jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration;

	// Decode the secret using Base64 (to match auth-service)
	private Key getSigningKey() {
	    byte[] keyBytes = Decoders.BASE64.decode(secret);
	    return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(String username) {
	    return Jwts.builder()
	            .setSubject(username)
	            .claim("roles", new String[]{"ADMIN"}) // must match claim used in SecurityConfig
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + expiration))
	            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
	            .compact();
	}

	public boolean validateToken(String token) {
	    try {
	        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
	        return true;
	    } catch (JwtException e) {
	        return false;
	    }
	}

	public String extractUsername(String token) {
	    return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
	            .parseClaimsJws(token).getBody().getSubject();
	}

}