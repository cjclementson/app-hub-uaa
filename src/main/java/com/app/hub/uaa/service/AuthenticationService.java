package com.app.hub.uaa.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

	private final UserDetailsService userDetailsService;

	public User registerUser(String email, String username, String password) {

		String encodedPassword = passwordEncoder.encode(password);
		String role = "Admin";
		return userRepository.save(new User(email, username, encodedPassword, role));
	}

	public User authenticateUser(String email, String password)  throws AuthenticationException {

		User user = (User) userDetailsService.loadUserByUsername(email);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("User details not found for the user {0}", email));
		}
		
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("Bad credentials");
		}

		return user;
	}

}
