package com.app.hub.uaa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.hub.uaa.controller.error.ControllerErrorResponse;
import com.app.hub.uaa.exception.UserAlreadyExistAuthenticationException;
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
	public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
		
		try {
			
			User user = authenticationService.registerUser(
				registrationRequest.getEmail(), 
				registrationRequest.getUsername(), 
				registrationRequest.getPassword());
		
			Map<String, Object> claims = new HashMap<>();
			claims.put("email", user.getEmail());
			
			var jwtToken = jwtService.generateToken(claims, user);
			
			return ResponseEntity.ok()
		        .header(HttpHeaders.AUTHORIZATION, jwtToken)
		        .body(new AuthenticationResponse(jwtToken));
		}
		catch (UserAlreadyExistAuthenticationException e) {
			return ResponseEntity.ok()
		        .body(new ControllerErrorResponse(e.getMessage()));			
		}
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
		
		try {
			
			User user = authenticationService.authenticateUser(
					authenticationRequest.getEmail(), 
					authenticationRequest.getPassword());
	
			Map<String, Object> claims = new HashMap<>();
			claims.put("email", user.getEmail());
			var jwtToken = jwtService.generateToken(claims, user);
			
			return ResponseEntity.ok()
			        .header(HttpHeaders.AUTHORIZATION, jwtToken)
			        .body(new AuthenticationResponse(jwtToken));
		}
		catch(UsernameNotFoundException e) {
			return ResponseEntity.ok()
			        .body(new ControllerErrorResponse(e.getMessage()));
		}
		catch(BadCredentialsException e) {
			return ResponseEntity.ok()
			        .body(new ControllerErrorResponse(e.getMessage()));			
		}
	}
	
}
