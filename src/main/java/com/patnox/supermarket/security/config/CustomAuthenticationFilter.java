package com.patnox.supermarket.security.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.*;
import org.springframework.http.*;
import org.springframework.web.filter.*;

import lombok.extern.slf4j.Slf4j;
import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

import com.patnox.supermarket.security.*;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
	private final AuthenticationManager authenticationManager;
	
	public CustomAuthenticationFilter(AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		log.info("Username is {}", username);
		log.info("Password is {}", password);
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		
		//return super.attemptAuthentication(request, response);
		return(authenticationManager.authenticate(authenticationToken));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		AppUser user = (AppUser) authResult.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256("security".getBytes());
		//Give a one day access token. Reduce for better security
		String accessToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);
		String refreshToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
				.withIssuer(request.getRequestURL().toString())
				.sign(algorithm);
		response.setHeader("accesstoken", accessToken);
		response.setHeader("refreshtoken", refreshToken);
		
		Map<String, String> tokens = new HashMap<>();
		tokens.put("accesstoken", accessToken);
		tokens.put("refreshtoken", refreshToken);
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}
	
	
}
