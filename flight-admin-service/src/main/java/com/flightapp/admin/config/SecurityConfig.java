package com.flightapp.admin.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class SecurityConfig {
	@Value("${spring.security.oauth2.resourceserver.jwt.secret}")
	private String jwtSecret;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	        	.requestMatchers("/auth/**").permitAll()
	            .requestMatchers("/admin/**").hasRole("ADMIN") // ✅ Only allow ROLE_ADMIN
	            .anyRequest().authenticated()
	        )
	        .oauth2ResourceServer(oauth2 -> oauth2
	            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
	        )
	        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

	    return http.build();
	}

	@Bean
	public JwtDecoder jwtDecoder() {
	byte[] decodedKey = java.util.Base64.getDecoder().decode(jwtSecret);
	return NimbusJwtDecoder.withSecretKey(
	new SecretKeySpec(decodedKey, "HmacSHA256")
	).build();
	}

	private JwtAuthenticationConverter jwtAuthenticationConverter() {
	    JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
	   converter.setAuthorityPrefix("");
	    converter.setAuthoritiesClaimName("roles");
	    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
	    jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
	    return jwtConverter;
	}

}