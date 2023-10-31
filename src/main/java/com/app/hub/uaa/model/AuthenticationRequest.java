package com.app.hub.uaa.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationRequest {
	
	private final String email;
	private final String password;
}
