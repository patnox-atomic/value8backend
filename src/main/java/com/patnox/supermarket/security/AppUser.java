package com.patnox.supermarket.security;

import java.util.Collection;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
//import org.hibernate.annotations.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;

import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.*;

@Entity(name = "users")
@Table(name = "users")
@Data
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class AppUser implements UserDetails
{
		@Id
		@Column(name = "id", unique = true, nullable = false)
		@SequenceGenerator(
			name = "user_sequence",
			sequenceName = "user_sequence",
			allocationSize = 1
		)
		@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "user_sequence"
		)
		private Long id;
		
		@Column(name = "firstname")
		private String firstName;
		
		@Column(name = "lastname")
		private String lastName;
		
		@Column(name = "email")
		private String email;
		
		@Column(name = "password")
		private String password;
		
//	    @Enumerated(EnumType.STRING)
//	    private AppUserRole appUserRole;
		@Column(name = "roles")
		@ManyToMany(fetch = FetchType.EAGER)
	    private Collection<AppUserRole> appUserRoles = new ArrayList<>();
		
		@Column(name = "locked" ,columnDefinition = "boolean default false")
	    private Boolean locked = false;
	    
	    @Column(name = "enabled" ,columnDefinition = "boolean default true")
	    private Boolean enabled = true;


//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRoles.name());
//        return Collections.singletonList(authority);
//	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		appUserRoles.forEach(role -> { 
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return(authorities);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public AppUserRole getAppUserRole() {
//		return appUserRole;
//	}
//
//	public void setAppUserRole(AppUserRole appUserRole) {
//		this.appUserRole = appUserRole;
//	}
	
	public Collection<AppUserRole> getAppUserRoles() {
		return appUserRoles;
	}

	public void setAppUserRoles(Collection<AppUserRole> appUserRoles) {
		this.appUserRoles = appUserRoles;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AppUser(String firstName, String lastName, String email, String password,
			Collection<AppUserRole> appUserRoles, Boolean locked, Boolean enabled) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.appUserRoles = appUserRoles;
		this.locked = locked;
		this.enabled = enabled;
	}

	public AppUser(String firstName, String lastName, String email, String password,
			Collection<AppUserRole> appUserRoles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.appUserRoles = appUserRoles;
	}


//	public AppUser(String firstName, String lastName, String email, String password, AppUserRole appUserRole,
//			Boolean locked, Boolean enabled) {
//		super();
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this.email = email;
//		this.password = password;
//		this.appUserRole = appUserRole;
//		this.locked = locked;
//		this.enabled = enabled;
//	}
//	
//	public AppUser(String firstName,
//            String lastName,
//            String email,
//            String password,
//            AppUserRole appUserRole) {
//		 this.firstName = firstName;
//		 this.lastName = lastName;
//		 this.email = email;
//		 this.password = password;
//		 this.appUserRole = appUserRole;
//	}
	
}
