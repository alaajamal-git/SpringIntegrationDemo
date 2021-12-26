package com.jms.system.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jms.system.services.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	UserService service;
	@Autowired
	Environment env;
	@Autowired
	JmsTemplate jms;
	@Value("${url.users.signup}")
	String signUpURL;
	@Value("${url.users.signin}")
	String signInURL;
	Logger logger= LoggerFactory.getLogger(WebSecurity.class);
	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		PasswordEncoder encoder=PasswordEncoderFactories.createDelegatingPasswordEncoder();
		builder.userDetailsService(service).passwordEncoder(encoder);
		
	}

	 @Override
	  protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();
			LoginRequestFilter filter = new LoginRequestFilter(service,env,jms);
			filter.setAuthenticationManager(authenticationManager());
			filter.setFilterProcessesUrl(signInURL);
			http.authorizeRequests()
	             .antMatchers(signUpURL).permitAll()
	             .and().addFilter(filter);	
		     http.headers().frameOptions().disable();

	      }
	  
}
