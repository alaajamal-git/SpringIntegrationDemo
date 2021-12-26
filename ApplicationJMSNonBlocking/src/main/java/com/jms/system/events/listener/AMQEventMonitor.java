package com.jms.system.events.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jms.system.events.models.UserSignUpSuccessEvent;
import com.jms.system.external.model.MongoPostModel;
import com.jms.system.metrics.UserMetrics;
import com.jms.system.services.ExtentionService;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class AMQEventMonitor {
	
	private final Logger logger=LoggerFactory.getLogger(AMQEventMonitor.class);
	@Autowired
	private UserMetrics counter;
	@Autowired
	ExtentionService service;
	@Autowired
	Environment env;
	WebClient client;

	@Autowired
	public AMQEventMonitor(Environment env,WebClient.Builder builder) {
		client= builder
				.baseUrl(env.getProperty("api.url"))
//	        .clientConnector(new ReactorClientHttpConnector(
//	                HttpClient.create().wiretap(true)
//	                ))
	        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
	        .build();
	}
	@JmsListener(destination = "${queue.event.signup_success}")
	private void onMessage3(UserSignUpSuccessEvent message) throws JsonProcessingException {
		counter.increaseCounter_3();
		MongoPostModel model=new MongoPostModel();
		model.setDataSource(env.getProperty("mongo.cluster"));
		model.setDatabase(env.getProperty("mongo.database"));
		model.setCollection(env.getProperty("mongo.collection"));
		model.setDocument(message);
		ObjectMapper mapper=new ObjectMapper();
		String body=mapper.writeValueAsString(model);
		
	    client.post().uri("/action/insertOne")
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.header("api-key",env.getProperty("api.secret.key"))
			.body(Mono.just(body), String.class)
			.retrieve()
			.onStatus(HttpStatus::isError, response->{
        		logger.error(response.toString());
                return Mono.error(new IllegalStateException(
                        String.format("Failed! %s", "Error")
                ));

			})
		.bodyToMono(String.class)
		.subscribeOn(Schedulers.single())
		.subscribe((x)->{
			System.out.println(Thread.currentThread().getName());
		});
	}
}