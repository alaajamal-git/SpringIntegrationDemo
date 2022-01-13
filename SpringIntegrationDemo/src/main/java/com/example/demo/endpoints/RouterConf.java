package com.example.demo.endpoints;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.router.RecipientListRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@MessageEndpoint
public class RouterConf {

	@ServiceActivator(inputChannel = "toRouterChannel")
	@Bean
	public RecipientListRouter activator() {
		RecipientListRouter router = new RecipientListRouter();
		    router.setSendTimeout(1_234L);
		    router.setIgnoreSendFailures(true);
		    router.setApplySequence(true);
		    router.addRecipient("infoChannel");
		    router.addRecipient("logChannel");
		    router.addRecipient("fileChannel");
		return router;
	}
	/*
	 * @Bean
	 * 
	 * @Router(inputChannel = "toRouterChannel") public AbstractMessageRouter
	 * myCustomRouter() {
	 * 
	 * return new AbstractMessageRouter() {
	 * 
	 * @Autowired
	 * 
	 * @Qualifier("fluxChannel") MessageChannel channel;
	 * 
	 * @Override protected Collection<MessageChannel>
	 * determineTargetChannels(Message<?> message) { Collection<MessageChannel>
	 * result = new ArrayList<>();
	 * if(message.getHeaders().get("type").equals("normal")) { result.add(channel);
	 * return result; }
	 * 
	 * return null; }
	 * 
	 * }; }
	 */
	
	@ServiceActivator(inputChannel = "errorChannel")
	public ResponseEntity<HttpStatus> error(Message<Exception> errmsg) {
		errmsg.getPayload().printStackTrace();
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Bean
	public MessageChannel logChannel() {
		return new QueueChannel();
	}
	
	@Bean
	public MessageChannel infoChannel() {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		return new ExecutorChannel(executor);
	}
	@Bean
	public MessageChannel fileChannel() {
		return new FluxMessageChannel();
	}
}
