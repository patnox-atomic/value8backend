package com.patnox.supermarket.security.config;

import com.patnox.supermarket.security.AppUserService;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.*;
import org.springframework.security.authentication.*;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                    .antMatchers("/api/v*/registration/**")
//                    .permitAll()
//                .anyRequest()
//                .authenticated().and()
//                .formLogin();
    	http.csrf().disable();
    	http.cors();
    	//http.authenticated().and().formLogin();
    	http.authorizeRequests().antMatchers("/api/refreshtoken/**").permitAll();
    	http.authorizeRequests().antMatchers("/api/v*/registration/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v*/order/**").hasAnyAuthority("RETAIL_ATTENDANT_ROLE", "WAREHOUSE_ATTENDANT_ROLE", "SUPERUSER_ROLE");
        http.authorizeRequests().antMatchers("/api/v*/product/**").hasAnyAuthority("RETAIL_ATTENDANT_ROLE", "WAREHOUSE_ATTENDANT_ROLE", "SUPERUSER_ROLE");
        http.authorizeRequests().antMatchers("/api/v*/sale/**").hasAnyAuthority("RETAIL_ATTENDANT_ROLE", "WAREHOUSE_ATTENDANT_ROLE", "SUPERUSER_ROLE");
        http.authorizeRequests().antMatchers("/api/v*/user/**").hasAnyAuthority("SUPERUSER_ROLE");
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
    	return(super.authenticationManagerBean());
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //corsConfiguration.setAllowedOrigins(Arrays.asList("https://example.com"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        //corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "accesstoken", "refreshtoken", "access_token", "refresh_token"));
        corsConfiguration.setExposedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "accesstoken", "refreshtoken", "access_token", "refresh_token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
