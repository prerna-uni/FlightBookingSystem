package com.flightapp.auth.service;

import com.flightapp.auth.model.User;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
	User getUserProfile(String jwt);
	public List<User>getAllUsers();

	

}
