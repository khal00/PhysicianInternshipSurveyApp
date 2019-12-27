package com.khal.intern_survey.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.khal.intern_survey.service.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private UserService userService;
    
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userService);
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.antMatchers("/admin/**").hasRole("ADMIN")
			.antMatchers("/oil/**").hasAnyRole("OILADMIN", "ADMIN")
			.antMatchers("/user/**").hasAnyRole("USER", "OILADMIN", "ADMIN")
			.antMatchers("/updatePassword*").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
			.antMatchers("/", "static/css", "static/images").permitAll()
			.and()
			.formLogin()
				.loginPage("/showLoginForm")
				.loginProcessingUrl("/authenticateTheUser")
				.defaultSuccessUrl("/")
				.permitAll()
			.and()
				.logout().logoutSuccessUrl("/?logout")
			.and()
				.exceptionHandling().accessDeniedPage("/access-denied");
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
		
}






