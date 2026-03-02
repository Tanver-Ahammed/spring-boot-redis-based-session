package com.session.config;

import com.session.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf()
				.disable()
				.authorizeRequests()
				.antMatchers("/", "/home")
				.permitAll()
				.antMatchers("/common")
				.authenticated()
				.antMatchers("/sa")
				.hasAuthority(Role.SUPER_ADMIN.name())
				.antMatchers("/ad")
				.hasAuthority(Role.ADMIN.name())
				.antMatchers("/op")
				.hasAuthority(Role.OPERATOR.name())
				.antMatchers("/test")
				.hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name(), Role.OPERATOR.name())
				.anyRequest()
				.authenticated()
				.and()
				.formLogin()
				.and()
				.httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication().withUser("Tanver")
				.password(passwordEncoder.encode("12345"))
				.authorities(Role.SUPER_ADMIN.name()).and().withUser("Ahammed")
				.password(passwordEncoder.encode("12345"))
				.authorities(Role.ADMIN.name()).and().withUser("Aminul")
				.password(passwordEncoder.encode("12345")).authorities(Role.OPERATOR.name());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return passwordEncoder;
	}
}
