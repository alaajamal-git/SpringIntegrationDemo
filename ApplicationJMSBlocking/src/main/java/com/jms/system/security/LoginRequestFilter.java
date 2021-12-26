package com.jms.system.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jms.system.events.models.EventType;
import com.jms.system.events.models.UserSignInFaildEvent;
import com.jms.system.events.models.UserSignInSuccessEvent;
import com.jms.system.models.UserSignInModel;
import com.jms.system.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class LoginRequestFilter extends UsernamePasswordAuthenticationFilter{
	private JmsTemplate jms;
	private UserService service;
	private Environment env;
	
	public LoginRequestFilter(UserService service,Environment env, JmsTemplate jms) {
		this.service=service;
		this.env=env;
		this.jms=jms;
	}

	public Authentication attemptAuthentication(HttpServletRequest req,HttpServletResponse res) throws AuthenticationException {
		UserSignInModel user = null;
		
		try {
			 user = new ObjectMapper().readValue(req.getInputStream(), UserSignInModel.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//use the authentication manager that use user service to access the database to authenticate the login request.
		Authentication authen= getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),new ArrayList<>()));
		 

		return authen;
	}
	
	@Override
    protected void successfulAuthentication(HttpServletRequest req,HttpServletResponse res,FilterChain chain,Authentication authResult) throws IOException, ServletException {
		jms.convertAndSend(env.getProperty("queue.event.signin_success"),new UserSignInSuccessEvent(EventType.SIGN_IN_SUCCESS_EVENT,new Date(System.currentTimeMillis()),(User)authResult.getPrincipal()));
		String id=service.getUserId(((User)authResult.getPrincipal()).getUsername());
		String token = Jwts.builder()
						.setSubject(id)
						.setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(env.getProperty("jwt.time.expired"))))
						.signWith(SignatureAlgorithm.HS256, env.getProperty("jwt.key.secret"))
						.compact();
		res.addHeader("userId", id);
		res.addHeader("access_token", token);
	}
	@Override
    protected void unsuccessfulAuthentication(HttpServletRequest req,HttpServletResponse res,AuthenticationException failed) throws IOException, ServletException {
		jms.convertAndSend(env.getProperty("queue.event.signin_faild"),new UserSignInFaildEvent(EventType.SIGN_IN_SUCCESS_EVENT,new Date(System.currentTimeMillis()),failed.getMessage().toString(),req.getRemoteAddr()));
		res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
