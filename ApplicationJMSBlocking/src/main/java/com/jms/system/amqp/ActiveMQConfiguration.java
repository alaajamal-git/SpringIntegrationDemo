package com.jms.system.amqp;

import javax.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class ActiveMQConfiguration {

	/**
	//this in case queue configuration
	@Bean
	public Queue loadJmsQueue() {
		return new ActiveMQTopic("local.queue");
	}
	*/
	
	@Bean
	public Queue loadJmsQueue1() {
		return new ActiveMQQueue("${queue.event.signin_success}");
	}
	@Bean
	public Queue loadJmsQueue2() {
		return new ActiveMQQueue("${queue.event.signin_faild}");
	}
	@Bean
	public Queue loadJmsQueue3() {
		return new ActiveMQQueue("${queue.event.signup_success}");
	}
	@Bean
	public Queue loadJmsQueue4() {
		return new ActiveMQQueue("${queue.event.signup_faild}");
	}
	@Bean
	public Queue loadJmsQueue5() {
		return new ActiveMQQueue("${queue.event.file_create}");
	}
	@Bean
	public Queue loadJmsQueue6() {
		return new ActiveMQQueue("${queue.event.file_reviewed}");
	}

}
