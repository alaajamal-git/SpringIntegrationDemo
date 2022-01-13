package com.example.demo.services;

import org.springframework.context.annotation.Bean;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.rsocket.ClientRSocketConnector;
import org.springframework.integration.rsocket.RSocketInteractionModel;
import org.springframework.integration.rsocket.dsl.RSockets;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.rsocket.RSocketStrategies;

import reactor.core.publisher.Mono;

@MessageEndpoint
public class RSocketConfig {
	
	@Bean
	public RSocketStrategies rsocketStrategies() {
	    return RSocketStrategies.builder()
	        .decoder(StringDecoder.textPlainOnly())
	        .encoder(CharSequenceEncoder.allMimeTypes())
	        .dataBufferFactory(new DefaultDataBufferFactory(true))
	        .build();
	}

	@Bean
	public ClientRSocketConnector clientRSocketConnector() {
	    ClientRSocketConnector clientRSocketConnector =new ClientRSocketConnector("localhost", 7000);
	    clientRSocketConnector.setRSocketStrategies(rsocketStrategies());
	    return clientRSocketConnector;
	}
	
	
	@Bean
	public IntegrationFlow rsocketFileFlow(ClientRSocketConnector clientRSocketConnector) {
	    return IntegrationFlows
	        .from("fileChannel")
	        .filter("{\"file\"}.contains(headers.get('type'))", e -> e.discardChannel("discardChannel"))
	        .log()
	        .handle(RSockets.outboundGateway("fileRoute")
	        		.clientRSocketConnector(clientRSocketConnector)
	                .interactionModel(RSocketInteractionModel.requestStream)
	                .expectedResponseType(String.class)
	                .clientRSocketConnector(clientRSocketConnector))
	        .channel("replyChannel") 
	        .get();
	}
	@Bean
	public MessageChannel discardChannel() {
		return new FluxMessageChannel();
	}
	
	@ServiceActivator(inputChannel = "discardChannel")
	public Mono<ResponseEntity<HttpStatus>> response(){
		return Mono.just(new ResponseEntity<>(HttpStatus.OK));
	}
}
