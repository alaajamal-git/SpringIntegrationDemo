package com.jms.system.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import com.jms.system.services.UserService;
@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurity {
	

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
	
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            ServerCodecConfigurer serverCodecConfigurer) {
        return http.addFilterAt(new LoginRequestFilter(authenticationManager(), serverCodecConfigurer, service,env),SecurityWebFiltersOrder.AUTHENTICATION)
			        .csrf()
			        .disable()
			        .build();
    }
    
    @Bean
    public UserDetailsRepositoryReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager manager =
                new UserDetailsRepositoryReactiveAuthenticationManager(service);
        return manager;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}