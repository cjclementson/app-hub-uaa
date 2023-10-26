package com.app.hub.uaa.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfiguration {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authManager(UserDetailsService detailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		
		provider.setUserDetailsService(detailsService);
		provider.setPasswordEncoder(passwordEncoder());
		
		return new ProviderManager(provider);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		return http.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
				
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
					
					List<String> origins = new ArrayList<>();
					origins.add("http://localhost:5500");// frontend hosted URL
					config.setAllowedOrigins(origins);
					
					List<String> methods = new ArrayList<>();
					methods.add("POST"); // for /login post from frontend
					// by default GET and HEAD is allowed
					config.setAllowedMethods(methods);
					
					/*List<String> headers = new ArrayList<>();
					headers.add("*");
					config.setAllowedHeaders(headers);*/
					
					//config.setMaxAge(3600L);
					
					return config;
				}
			}))
			.authorizeHttpRequests((requests) -> requests
			.requestMatchers("/api/v1/user/**").authenticated()
			.requestMatchers("/login").permitAll()
			.requestMatchers("/api/v1/auth/**").permitAll())
			.formLogin(form -> 
				form.loginPage("http://localhost:5500/login-html-css/index.html")
					.loginProcessingUrl("/login")
					.defaultSuccessUrl("/api/v1/user/2").permitAll())
			.httpBasic(withDefaults())
			.build();
	}

}
