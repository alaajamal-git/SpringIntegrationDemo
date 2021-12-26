package com.jms.system.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.jms.system.models.UserSignUpModel;
import com.jms.system.models.UserOutBound;

public interface UserService extends UserDetailsService {
	
	public UserOutBound userSignUp(UserSignUpModel user);
	public String getUserId(String username);
	
}
