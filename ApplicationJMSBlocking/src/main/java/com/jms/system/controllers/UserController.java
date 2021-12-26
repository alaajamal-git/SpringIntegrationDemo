package com.jms.system.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jms.system.events.models.EventType;
import com.jms.system.events.models.UserSignUpFaildEvent;
import com.jms.system.events.models.UserSignUpSuccessEvent;
import com.jms.system.models.UserOutBound;
import com.jms.system.models.UserSignUpModel;
import com.jms.system.services.UserService;

@RestController
@RequestMapping("/jms/app/user")
public class UserController {
	@Value("${queue.event.signup_success}")
	String event_3_url;
	@Value("${queue.event.signup_faild}")
	String event_4_url;
	@Autowired
	JmsTemplate jms;
	@Autowired
	UserService userService;
	
	@PostMapping("/signup")
	private ResponseEntity<?> signUpUser(@Valid @RequestBody UserSignUpModel user, Errors error, HttpServletRequest req){
		UserOutBound res = userService.userSignUp(user);
		if(error.getErrorCount()>0) {
			jms.convertAndSend(event_4_url,new UserSignUpFaildEvent(EventType.SIGN_UP_FIALD_EVENT,new Date(System.currentTimeMillis()),user,req.getRemoteAddr()));
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else
			jms.convertAndSend(event_3_url,new UserSignUpSuccessEvent(EventType.SIGN_UP_SUCEESS_EVENT,new Date(System.currentTimeMillis()),user));
		return new ResponseEntity<>(res,HttpStatus.CREATED);
	}
}
