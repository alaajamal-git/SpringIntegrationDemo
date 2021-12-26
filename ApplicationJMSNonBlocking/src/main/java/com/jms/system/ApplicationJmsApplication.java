package com.jms.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableFeignClients
@EnableWebFlux
public class ApplicationJmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationJmsApplication.class, args);
	}
	
}
