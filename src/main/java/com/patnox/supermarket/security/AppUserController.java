package com.patnox.supermarket.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patnox.supermarket.orders.Order;
import com.patnox.supermarket.products.Product;
import com.patnox.supermarket.products.ProductService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URI;
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
	public ResponseEntity<AppUser> createNewUser(@RequestBody AppUser newUser)
	{
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/v1/user").toUriString());
		return ResponseEntity.created(uri).body(appUserService.saveUser(newUser));
	}
	
	@PostMapping("/v1/user/role")
	public ResponseEntity<AppUserRole> createNewRole(@RequestBody AppUserRole newRole)
	{
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/v1/user/role").toUriString());
		return ResponseEntity.created(uri).body(appUserService.saveRole(newRole));
	}
	
	@PostMapping("/v1/user/addrole")
	public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserFrom addRole)
	{
		appUserService.addRoleToUser(addRole.getUsername(), addRole.getRolename());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/v1/user")
	public List<AppUser> getAllUsers() {
		return appUserService.getAllAppUsers();
	}
	
	@GetMapping("/v1/user/role")
	public List<AppUserRole> getAllRoles() {
		return appUserService.getAllAppUserRoles();
	}
	
	@GetMapping(path = "/v1/user/{userId}")
	public AppUser getUser(@PathVariable("userId") Long userId) {
		return appUserService.getUser(userId).orElseThrow(() -> new IllegalStateException("User with ID: " + userId + " does not exist"));
	}
	
	@DeleteMapping(path = "/v1/user/{userId}")
	public void deleteUser(@PathVariable("userId") Long userId)
	{
		appUserService.deleteUser(userId);
	}
	
	@PutMapping(path = "/v1/user/{userId}")
	public void updateUser(@PathVariable("userId") Long userId,
				@RequestParam(required = false) String firstName,
				@RequestParam(required = false) String lastName,
				@RequestParam(required = false) String email,
				@RequestParam(required = false) String password,
				@RequestParam(required = false) Collection<AppUserRole> appUserRoles,
				@RequestParam(required = false) Boolean locked,
				@RequestParam(required = false) Boolean enabled
			)
	{
		appUserService.updateUser(userId, firstName, lastName, email, password, appUserRoles, locked, enabled);
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
						.withClaim("username", user.getFirstName())
						.withExpiresAt(new Date(System.currentTimeMillis() +(24 * 60 * 60 * 1000)))
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
	
	@Data
	class RoleToUserFrom
	{
		private String username;
		private String rolename;
	}
}
