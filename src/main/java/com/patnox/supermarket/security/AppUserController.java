package com.patnox.supermarket.security;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patnox.supermarket.orders.Order;
import com.patnox.supermarket.products.Product;
import com.patnox.supermarket.products.ProductService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.time.*;

@RestController
@RequestMapping(path = "api")
@Slf4j
public class AppUserController 
{
	private final AppUserService appUserService;
	
	@Autowired
	public AppUserController(AppUserService appUserService) {
		this.appUserService = appUserService;
	}
	
	@PostMapping("/v1/user")
	public void createNewUser(@RequestBody AppUser newUser)
	{
		newUser.setAppUserRole(AppUserRole.USER_ROLE);
		newUser.setEnabled(true);
		//log.error("Got FirstName: {}", newUser.getFirstName());
		appUserService.signUpUser(newUser);
	}

	@GetMapping("/v1/user")
	public List<AppUser> getAll() {
		return appUserService.getAllAppUsers();
	}
	
	@GetMapping("/refreshtoken")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
		{
			try
			{
				String refreshToken = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("security".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refreshToken);
				String username = decodedJWT.getSubject();
				AppUser user = appUserService.getUser(username).orElseThrow();
				String accessToken = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() +(10 * 60 * 1000)))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())) //check on this
						.sign(algorithm);
				response.setHeader("accesstoken", accessToken);
				response.setHeader("refreshtoken", refreshToken);
				
				Map<String, String> tokens = new HashMap<>();
				tokens.put("accesstoken", accessToken);
				tokens.put("refreshtoken", refreshToken);
				
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				log.error("Got Error Refreshing Token: {}", e.getMessage());
				response.setHeader("error", e.getMessage());
				response.setStatus(HttpStatus.FORBIDDEN.value());
				Map<String, String> error = new HashMap<>();
				error.put("error_message", e.getMessage());
				
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		}
		else
		{
			throw new RuntimeException("Refresh Token Is Missing");
		}
	}
}
