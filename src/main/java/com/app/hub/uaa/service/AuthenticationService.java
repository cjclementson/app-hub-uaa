package com.app.hub.uaa.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.hub.uaa.model.User;
import com.app.hub.uaa.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public User registerUser(String email, String username, String password) {
		
		String encodedPassword = passwordEncoder.encode(password);
		String role = "Admin";
		return userRepository.save(new User(email, username, encodedPassword, role));
	}

}
