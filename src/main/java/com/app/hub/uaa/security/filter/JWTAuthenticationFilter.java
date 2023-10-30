package com.app.hub.uaa.security.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.hub.uaa.model.User;
import com.app.hub.uaa.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
	
	private final JWTService jwtService;
	
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, // intercept request to extract information 
			@NonNull HttpServletResponse response, // add information to response
			@NonNull FilterChain filterChain) // list of other filters
			throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization");
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			filterChain.doFilter(request, response);
			return;
		}
		
		final String jwtToken = authHeader.substring(7);
		var email = jwtService.getEmail(jwtToken);
		
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null /* user not logged in */) {
			
			var userDetails = (User)userDetailsService.loadUserByUsername(email);
			
			if (jwtService.isTokenValid(jwtToken, userDetails)) {
				
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						userDetails.getAuthorities());
				
				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			
		}
		
		filterChain.doFilter(request, response);
	}
}
