package com.jms.system.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.jms.system.models.UserEntityModel;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntityModel, Long>{
    @Query("SELECT * FROM user_entity_model WHERE id = :id")
	public Mono<UserEntityModel> findByUserId(String id);
    @Query("SELECT * FROM user_entity_model WHERE username = :username")
	public Mono<UserEntityModel> findByUsername(String username);
    @Query("SELECT user_id FROM user_entity_model WHERE username = :username")
	public Mono<String> findUserId(String username);
}
