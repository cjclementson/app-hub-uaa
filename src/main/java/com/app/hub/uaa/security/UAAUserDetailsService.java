package com.app.hub.uaa.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.hub.uaa.model.User;
import com.app.hub.uaa.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UAAUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		var users = userRepository.findByEmail(username);
		
		if (!users.isPresent())
		{
			throw new UsernameNotFoundException(String.format("User details not found for the user {0}", username));			
		}
		else
		{
			long userId = users.get().getUserId();
			String userName = users.get().getUsername();
			String email = users.get().getEmail();
			String password = users.get().getPassword();
			String role = users.get().getRole();
			java.sql.Timestamp createdOn = users.get().getCreatedOn();
			java.sql.Timestamp lastLogin = users.get().getLastLogin();
			
			return new User(userId, email, userName, password, role, createdOn, lastLogin);
		}		
	}
}
