package com.example.demo.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.expression.FunctionExpression;
import org.springframework.integration.r2dbc.dsl.R2dbc;
import org.springframework.integration.r2dbc.outbound.R2dbcMessageHandler;
import org.springframework.messaging.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.r2dbc.spi.ConnectionFactory;

@MessageEndpoint
public class H2Config {
	
	@Autowired
	ConnectionFactory h2Connection;
	
	@Bean
	public IntegrationFlow h2Flow() {
		return IntegrationFlows.from("infoChannel")
		        .filter(Message.class, m -> m.getHeaders().get("http_requestMethod").equals("POST") &&  m.getHeaders().get("type").equals("info"))
				.handle(R2dbc.outboundChannelAdapter(new R2dbcEntityTemplate(h2Connection))
				        .queryType(R2dbcMessageHandler.Type.INSERT)
				        .tableName("message_entity_model")
				        .values(new FunctionExpression<Message<?>>(msg->{
				        	ObjectMapper mapper = new ObjectMapper();
				        	try {
								return mapper.readValue(msg.getPayload().toString(), Map.class);
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
				        	return null;
				        })))
				
				       // .values("{id: 1, content: 'Hello '}"))
				.get()
			;
	}
}
