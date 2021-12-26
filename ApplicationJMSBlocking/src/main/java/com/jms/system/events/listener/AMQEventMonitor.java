package com.jms.system.events.listener;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jms.system.events.models.UserSignInFaildEvent;
import com.jms.system.events.models.UserSignInSuccessEvent;
import com.jms.system.events.models.UserSignUpFaildEvent;
import com.jms.system.events.models.UserSignUpSuccessEvent;
import com.jms.system.external.model.MongoPostModel;
import com.jms.system.metrics.UserMetrics;
import com.jms.system.services.ExtentionService;

@Component
public class AMQEventMonitor {
	
	private final Logger logger=LoggerFactory.getLogger(AMQEventMonitor.class);
	@Autowired
	private UserMetrics counter;
	@Autowired
	ExtentionService service;
	@Autowired
	Environment env;
	@Autowired
	RestTemplate restTemplate;
	// in case using topics we should keep listener concurrency to 1
	@JmsListener(destination = "${queue.event.signin_success}")
	@Transactional
	private void onMessage1(UserSignInSuccessEvent msg) {
		logger.info(msg.toString());
		counter.increaseCounter_1();
	}
	@JmsListener(destination = "${queue.event.signin_faild}", concurrency = "1-2")
	@Transactional
	private void onMessage2(UserSignInFaildEvent msg) {
		logger.info(msg.toString());
		counter.increaseCounter_2();
	}
	@JmsListener(destination = "${queue.event.signup_success}")
	@Transactional
	private void onMessage3(UserSignUpSuccessEvent event) throws JsonProcessingException {
			counter.increaseCounter_3();
			MongoPostModel model=new MongoPostModel();
			model.setDataSource(env.getProperty("mongo.cluster"));
			model.setDatabase(env.getProperty("mongo.database"));
			model.setCollection(env.getProperty("mongo.collection"));
			model.setDocument(event);
			ObjectMapper objectMapper = new ObjectMapper();
			 HttpHeaders headers = new HttpHeaders();
		      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		      headers.set("api-key", env.getProperty("api.secret.key"));
		      HttpEntity<String> entity = new HttpEntity<String>(objectMapper.writeValueAsString(model),headers);
		      CompletableFuture.supplyAsync(()->{
		    	  System.out.println("thread: "+Thread.currentThread().getName());
			     return restTemplate.exchange(env.getProperty("api.url"), HttpMethod.POST, entity, String.class).getStatusCode();
		      }).thenAccept((x)->{
		    	  System.out.println(Thread.currentThread().getName());
		      });
		     
	}
	@JmsListener(destination = "${queue.event.signup_faild}", concurrency = "1-2")
	@Transactional
	private void onMessage4(UserSignUpFaildEvent msg) {
		logger.info(msg.toString());
		counter.increaseCounter_4();
	}
	@JmsListener(destination = "${queue.event.file_create}")
	@Transactional
	private void onMessage5(UserSignInSuccessEvent msg) {
		logger.info(msg.toString());
	}
	@JmsListener(destination = "${queue.event.file_reviewed}")
	@Transactional
	private void onMessage6(UserSignInSuccessEvent msg) {
		logger.info(msg.toString());
	}
	
}