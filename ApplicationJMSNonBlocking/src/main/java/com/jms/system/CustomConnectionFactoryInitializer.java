package com.jms.system;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.function.client.WebClient;

import io.r2dbc.spi.ConnectionFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class CustomConnectionFactoryInitializer {
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
    @Bean
	public WebClient.Builder webClientBuilder() {
	    String connectionProviderName = "WebConnectionProvider";
	    ConnectionProvider conn = ConnectionProvider.builder(connectionProviderName).maxConnections(200).pendingAcquireMaxCount(-1).pendingAcquireTimeout(Duration.ofSeconds(3000)).build();
	    HttpClient httpClient = HttpClient.create(conn);
	    return WebClient.builder()
	            .clientConnector(new ReactorClientHttpConnector(httpClient));
	}
    /*
	 * @Bean public DefaultMessageListenerContainer customMessageListenerContainer(
	 * ConnectionFactory connectionFactory, MessageListener
	 * queueListener, @Value("${queue.event.signup_success}") final String
	 * destinationName){ DefaultMessageListenerContainer listener = new
	 * DefaultMessageListenerContainer();
	 * listener.setConnectionFactory(connectionFactory);
	 * listener.setDestinationName(destinationName);
	 * listener.setMessageListener(queueListener); return listener; }
	 */
}
