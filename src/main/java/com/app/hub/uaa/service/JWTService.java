package com.app.hub.uaa.service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.app.hub.uaa.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	
	private static final String SECRET_KEY = "79af958e14ae8dd5fd9ef3d6c26f7684092b4d83e2f8837b3c8d832872bae914";
	
	public boolean isTokenValid(String token) {
		
		extractClaims(token);
		return !isTokenExpired(token);
	}

	public boolean isTokenValid(String token, User userDetails) {
		
		final String tokenEmail = getEmail(token);
		final String userEmail = userDetails.getEmail();
		return tokenEmail.equals(userEmail) && !isTokenExpired(token);
	}

	public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
		
		return Jwts
				.builder()
				.setClaims(claims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24)/* 24 hours */))
				.signWith(getKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public String generateToken(UserDetails userDetails) {
		
		return Jwts
				.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24)))
				.signWith(getKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String getEmail(String token) {
		var claims = extractClaims(token);
		return String.valueOf(claims.get("email"));
	}
	
	private Claims extractClaims(String token) {

		return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getKey() {
		byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyByte);
	}
	
	private boolean isTokenExpired(String token) {
		var claims = extractClaims(token);
		var expirationDate = claims.getExpiration();
		
		if (expirationDate.before(new Date())) {
			return true;
		}
		
		return false;
	}

}
