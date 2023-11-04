package com.app.hub.uaa.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.hub.uaa.exception.UserAlreadyExistAuthenticationException;
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
		
		var user = userRepository.findByEmail(email);
		
		if(user.isPresent()) {
			throw new UserAlreadyExistAuthenticationException(
					"There was a problem creating your account. Check that your email address is spelled correctly.");
		}

		String encodedPassword = passwordEncoder.encode(password);
		String role = "Admin";
		return userRepository.save(new User(email, username, encodedPassword, role));
	}

	public User authenticate(String email, String password)  throws AuthenticationException {

		User user = (User) userDetailsService.loadUserByUsername(email);
		
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException(
					"There was a problem logging in. Check your email and password or create an account.");
		}

		return user;
	}

}
