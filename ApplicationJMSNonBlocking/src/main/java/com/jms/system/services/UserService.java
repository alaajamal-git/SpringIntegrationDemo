package com.jms.system.services;

import com.jms.system.models.UserSignUpModel;

import reactor.core.publisher.Mono;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import com.jms.system.models.UserOutBound;

public interface UserService extends ReactiveUserDetailsService {
	
	public Mono<UserOutBound> userSignUp(UserSignUpModel user);
	public Mono<String> getUserId(String username);
	
}
