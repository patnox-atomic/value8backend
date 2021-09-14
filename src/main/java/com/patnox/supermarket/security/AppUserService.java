package com.patnox.supermarket.security;

import com.patnox.supermarket.orders.Order;
import com.patnox.supermarket.products.Product;
import com.patnox.supermarket.registration.token.ConfirmationToken;
import com.patnox.supermarket.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
	private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final AppUserRoleRepository appUserRoleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }
    
    public AppUser saveUser(AppUser user)
    {
    	boolean userExists = appUserRepository
                .findByEmail(user.getEmail())
                .isPresent();

        if (userExists) 
        {
            throw new IllegalStateException("Error: email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        
        user.setLocked(false);
        user.setEnabled(true);

        appUserRepository.save(user);
        return(user);
    }
    
    public AppUserRole saveRole(AppUserRole role)
    {
    	boolean roleExists = appUserRoleRepository.findByName(role.getName()).isPresent();
    	
    	if (roleExists) 
        {
            throw new IllegalStateException("Error: role already exists");
        }
    	
    	appUserRoleRepository.save(role);
        return(role);
    }
    
    public List<AppUser> getAllAppUsers() 
	{
	    return appUserRepository.findAll();
	}
    
    public Optional<AppUser> getUser(String username)
    {
    	return appUserRepository.findByEmail(username);
    }
    
    public Optional<AppUser> getUser(Long userId)
    {
    	return appUserRepository.findById(userId);
    }
    
    public List<AppUserRole> getAllAppUserRoles() 
	{
	    return appUserRoleRepository.findAll();
	}
    
    public Optional<AppUserRole> getUserRole(String name)
    {
    	return appUserRoleRepository.findByName(name);
    }
    
    @Transactional
	public void deleteUser(Long userId)
	{
		System.out.println("Request to delete User ID: " + userId);
		boolean exists = appUserRepository.existsById(userId);
		if(!exists)
		{
			System.err.println("Error: User with ID: " + userId + " does not exist");
			throw new IllegalStateException("User with ID: " + userId + " does not exist");
		}
		else
		{
			System.out.println("User with ID: " + userId + " exists so we will proceed");
			AppUser victimizedUser = appUserRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User with ID: " + userId + " does not exist"));
			victimizedUser.setEnabled(false);
			victimizedUser.setLocked(true);
		}
	}
    
    @Transactional
	public void updateUser(Long userId, String firstName, String lastName, String email, String password, Collection<AppUserRole> appUserRoles, Boolean locked, Boolean enabled)
	{
		System.out.println("Request to update user ID: " + userId);
		boolean exists = appUserRepository.existsById(userId);
		if(!exists)
		{
			System.err.println("Error: User with ID: " + userId + " does not exist");
			throw new IllegalStateException("User with ID: " + userId + " does not exist");
		}
		else
		{
			System.out.println("User with ID: " + userId + " exists so we will proceed");
			AppUser victimizedUser = appUserRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User with ID: " + userId + " does not exist"));
			if(appUserRoles != null && appUserRoles.size() > 0) victimizedUser.setAppUserRoles(appUserRoles);
			if(firstName != null && firstName.length() > 0) victimizedUser.setFirstName(firstName);
			if(lastName != null && lastName.length() > 0) victimizedUser.setLastName(lastName);
			if(email != null && email.length() > 0) victimizedUser.setEmail(email);
			if(locked != null) victimizedUser.setLocked(locked);
			if(enabled != null) victimizedUser.setEnabled(enabled);
			if(password != null && password.length() > 0)
			{
				String encodedPassword = bCryptPasswordEncoder.encode(password);
				victimizedUser.setPassword(encodedPassword);
			}
		}
	}
    
    @Transactional
    public void addRoleToUser(String username, String role)
    {
    	AppUserRole theRole = appUserRoleRepository.findByName(role).orElseThrow(() -> new IllegalStateException("Error: role does not exist"));
    	AppUser theUser = appUserRepository.findByEmail(username).orElseThrow(() -> new IllegalStateException("Error: user does not exist"));
    	Collection<AppUserRole> m = theUser.getAppUserRoles();
    	if(!m.contains(theRole))
    	{
	    	m.add(theRole);
	    	theUser.setAppUserRoles(m);
    	}
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if (userExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.

            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

//        TODO: SEND EMAIL

        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    	//return(0);
    }
}
