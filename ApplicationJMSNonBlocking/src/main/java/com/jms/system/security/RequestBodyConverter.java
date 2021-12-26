package com.jms.system.security;

import java.util.Collections;

import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;

import com.jms.system.models.UserSignInModel;

import reactor.core.publisher.Mono;

public class RequestBodyConverter  implements ServerAuthenticationConverter{

    private final ResolvableType loginType = ResolvableType.forClass(UserSignInModel.class);

	private CodecConfigurer serverCodecConfigurer;
	public RequestBodyConverter(ServerCodecConfigurer serverCodecConfigurer) {
		this.serverCodecConfigurer=serverCodecConfigurer;
	}
	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		 return serverCodecConfigurer.getReaders().stream()
	                .filter(reader -> reader.canRead(this.loginType, MediaType.APPLICATION_JSON))
	                .findFirst()
	                .orElseThrow(() -> new IllegalStateException("No JSON reader for UsernamePasswordContent"))
	                .readMono(this.loginType, exchange.getRequest(), Collections.emptyMap())
	                .cast(UserSignInModel.class)
	                .map(o -> new UsernamePasswordAuthenticationToken(o.getUsername(), o.getPassword()));
			
	}

}
