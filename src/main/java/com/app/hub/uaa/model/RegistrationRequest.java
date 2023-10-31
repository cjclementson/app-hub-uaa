package com.app.hub.uaa.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegistrationRequest {
	
	private final String email;
	private final String username;
	private final String password;

}
