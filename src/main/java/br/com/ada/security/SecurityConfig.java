package br.com.ada.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String PLAYER = "PLAYER";
	private static final String ADMIN = "ADMIN";

	private static final String[] PUBLIC_MATCHERS = { "/ada/login", };
	private static final String[] GET_PUBLIC_MATCHERS = { "/ada/rank/**", "/ada/movie/**" };

	private static final String[] USER_MATCHERS = { "/ada/battle/**" };
	private static final String[] ADMIN_MATCHERS = { "/ada/h2-console/**", "/ada/player/**", "/ada/movie/**" };

	private final CurrentUserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeHttpRequests().antMatchers(PUBLIC_MATCHERS).permitAll()
				.antMatchers(HttpMethod.GET, GET_PUBLIC_MATCHERS).permitAll().antMatchers(USER_MATCHERS).hasRole(PLAYER)
				.antMatchers(ADMIN_MATCHERS).hasRole(ADMIN).and().headers().frameOptions().sameOrigin().and()
				.addFilter(new JWTAuthenticationFilter(authenticationManager()))
				.addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService));

	}

	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/css/**", "/js/**");
	}

	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

}
