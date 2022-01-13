package com.example.demo.endpoints;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.messaging.MessageChannel;

@Configuration
public class HttpGateWay {

	@Bean
	public IntegrationFlow postHttpGateWay() {
		return IntegrationFlows.from(
				WebFlux.inboundGateway("/{type}/data")
				.requestPayloadType(String.class)
				.headerExpression("type", "#pathVariables.type")
				.replyChannel("replyChannel")
				.errorChannel("errorChannel")
				)
				.channel("toRouterChannel")
				.get();
	}
	
	
	@Bean
	public MessageChannel replyChannel() {
		return new FluxMessageChannel();
	}
	@Bean
	public MessageChannel errorChannel() {
		return new DirectChannel();
	}
	@Bean
	public MessageChannel toRouterChannel() {
		return new DirectChannel();
	}
	
}
