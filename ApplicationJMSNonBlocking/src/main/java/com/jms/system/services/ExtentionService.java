package com.jms.system.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name= "mongo",url="${api.url}",path="/action/insertOne")
public interface ExtentionService {
	
	final Logger logger= LoggerFactory.getLogger(ExtentionService.class);
	
	@PostMapping
	//@CircuitBreaker(name = "MongoBackend", fallbackMethod = "fallback")
	//@Retry(name="MongoBackend", fallbackMethod = "fallback")
	public void postEvent(String event, @RequestHeader("api-key") String authen);
	
	@GetMapping
	public ResponseEntity<?> pingServer();
	
	 default void fallback(String event,String body, Exception e) {
		logger.error("Retry fallBack!");
		logger.error(event);
	}
}
