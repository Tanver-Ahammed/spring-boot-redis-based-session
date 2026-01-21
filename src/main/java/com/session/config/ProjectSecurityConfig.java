package com.session.config;

import com.session.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/home")
						.permitAll()
						.requestMatchers("/common")
						.authenticated()
						.requestMatchers("/sa")
						.hasAuthority(Role.SUPER_ADMIN.name())
						.requestMatchers("/ad")
						.hasAuthority(Role.ADMIN.name())
						.requestMatchers("/op")
						.hasAuthority(Role.OPERATOR.name())
						.requestMatchers("/test")
						.hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name(), Role.OPERATOR.name())
						.anyRequest()
						.authenticated())
				// Updated to modern Customizer syntax
				.formLogin(Customizer.withDefaults())
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
		// Passwords must now be encoded with BCrypt
		UserDetails ta = User.withUsername("Tanver")
				.password(passwordEncoder.encode("12345"))
				.authorities(Role.SUPER_ADMIN.name())
				.build();

		UserDetails ah = User.withUsername("Ahammed")
				.password(passwordEncoder.encode("12345"))
				.authorities(Role.ADMIN.name())
				.build();

		UserDetails am = User.withUsername("Aminul")
				.password(passwordEncoder.encode("12345"))
				.authorities(Role.OPERATOR.name())
				.build();

		return new InMemoryUserDetailsManager(ta, ah, am);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// Replaced NoOpPasswordEncoder with BCrypt for security
		return new BCryptPasswordEncoder();
	}
}
