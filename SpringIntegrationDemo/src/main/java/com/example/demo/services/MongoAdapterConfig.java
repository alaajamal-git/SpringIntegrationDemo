package com.example.demo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoClientFactoryBean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mongodb.dsl.MongoDb;
import org.springframework.messaging.Message;

@MessageEndpoint
public class MongoAdapterConfig {
	@Value("${project.mongo.string}")
	String connectionString;
	@Bean
	 public  ReactiveMongoClientFactoryBean mongoDbFactory() {
         ReactiveMongoClientFactoryBean clientFactory = new ReactiveMongoClientFactoryBean();
         clientFactory.setConnectionString(connectionString);

         return clientFactory;
    }
	@Bean
	public IntegrationFlow reactiveMongoDbFlow(ReactiveMongoDatabaseFactory mongoDbFactory) {
	    return f -> f
	            .channel("logChannel")
		        .filter(Message.class, m -> m.getHeaders().get("http_requestMethod").equals("POST") && m.getHeaders().get("type").equals("logs"))
	            .handle(MongoDb.reactiveOutboundChannelAdapter(mongoDbFactory));
	}
}
