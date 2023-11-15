package com.app.hub.uaa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.hub.uaa.model.User;
import com.app.hub.uaa.repository.UserRepository;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
	
	private final UserRepository userRepository;	
	
	@GetMapping("/{userId}")
	public User getUser(@PathVariable long userId) {
		
		return userRepository.findById(userId).get();
	}

}
