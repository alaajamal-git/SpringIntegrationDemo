package com.jms.system.security;

import java.util.Date;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import com.jms.system.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import reactor.core.publisher.Mono;

public class LoginRequestFilter extends AuthenticationWebFilter implements ServerAuthenticationSuccessHandler {
	private JmsTemplate jms;
	private UserService service;
	private Environment env;

	public LoginRequestFilter(ReactiveAuthenticationManager authenticationManager,ServerCodecConfigurer serverCodecConfigurer,UserService service, Environment env) {
		super(authenticationManager);
		this.service=service;
		this.env=env;
		setRequiresAuthenticationMatcher(
				ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/jms/app/user/signin"));
		setServerAuthenticationConverter(new RequestBodyConverter(serverCodecConfigurer));
		setAuthenticationSuccessHandler(this);
	}

	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
		service.getUserId(((User)authentication.getPrincipal()).getUsername())
		.map(x->{
			return Jwts.builder()
			.setSubject(x)
			.setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(env.getProperty("jwt.time.expired"))))
			.signWith(SignatureAlgorithm.HS256, env.getProperty("jwt.key.secret"))
			.compact();
		})
		.doOnNext((tk)->{
			webFilterExchange.getExchange().getResponse().getHeaders().setBearerAuth(tk);
			webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.OK);
		}).subscribe();
		

		return Mono.empty();
	}

}