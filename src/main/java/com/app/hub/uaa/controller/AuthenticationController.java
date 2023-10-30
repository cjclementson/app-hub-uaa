package com.app.hub.uaa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.hub.uaa.model.AuthenticationResponse;
import com.app.hub.uaa.model.User;
import com.app.hub.uaa.service.AuthenticationService;
import com.app.hub.uaa.service.JWTService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
@AllArgsConstructor
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;
	
	private final JWTService jwtService;
	
	@PostMapping("/register")
	public AuthenticationResponse register(@RequestBody User user) {
		authenticationService.registerUser(user.getEmail(), user.getUsername(), user.getPassword());
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", user.getEmail());
		
		var jwtToken = jwtService.generateToken(claims, user);
		var auth = new AuthenticationResponse(jwtToken);
		return auth;
		
		//return "user registered!";
	} 
}
