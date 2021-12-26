package com.jms.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jms.system.models.UserEntityModel;

@Repository
public interface UserRepository extends JpaRepository<UserEntityModel, Long>{
	
	public UserEntityModel findByUserId(String id);
	public UserEntityModel findByUsername(String username);

}
