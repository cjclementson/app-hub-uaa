package com.app.hub.uaa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.hub.uaa.model.AuthenticationRequest;
import com.app.hub.uaa.model.AuthenticationResponse;
import com.app.hub.uaa.model.RegistrationRequest;
import com.app.hub.uaa.model.User;
import com.app.hub.uaa.service.AuthenticationService;
import com.app.hub.uaa.service.JWTService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	private final JWTService jwtService;

	@PostMapping("/register")
	public AuthenticationResponse register(@RequestBody RegistrationRequest registrationRequest) {
		User user = authenticationService.registerUser(
				registrationRequest.getEmail(), 
				registrationRequest.getUsername(), 
				registrationRequest.getPassword());

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", user.getEmail());

		var jwtToken = jwtService.generateToken(claims, user);
		var auth = new AuthenticationResponse(jwtToken);
		return auth;
	}

	@PostMapping("/authenticate")
	public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
		User user = authenticationService.authenticateUser(
				authenticationRequest.getEmail(), 
				authenticationRequest.getPassword());

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", user.getEmail());
		var jwtToken = jwtService.generateToken(claims, user);
		var auth = new AuthenticationResponse(jwtToken);
		return auth;
	}
}
