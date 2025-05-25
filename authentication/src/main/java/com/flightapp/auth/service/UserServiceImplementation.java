package com.flightapp.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.flightapp.auth.model.User;
import org.springframework.stereotype.Service;
import com.flightapp.auth.config.JwtProvider;
import com.flightapp.auth.repository.UserRepository;
@Service
public class UserServiceImplementation implements UserService{
	@Autowired
	private UserRepository userRepository;

	@Override
	public User getUserProfile(String jwt) {
	         String email = JwtProvider.getEmailFromJwtToken(jwt);
	         return userRepository.findByEmail(email);
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

}
