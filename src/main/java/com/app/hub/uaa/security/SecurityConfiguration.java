package com.app.hub.uaa.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.text.Normalizer.Form;

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
			//.cors(cors -> cors.disable())
			.authorizeHttpRequests((requests) -> requests
			.requestMatchers("/api/v1/user/**").authenticated()
			.requestMatchers("/login").permitAll()
			.requestMatchers("/api/v1/auth/**").permitAll())
			.formLogin(form -> 
				form.loginPage("http://localhost:5500/login-html-css/index.html")
					.loginProcessingUrl("/login")
					.defaultSuccessUrl("/api/v1/user/2").permitAll())
			//.formLogin(withDefaults())
			.httpBasic(withDefaults())
			.build();
	}

}
