package com.jms.system.services.iml;

import java.util.ArrayList;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.jms.system.models.UserEntityModel;
import com.jms.system.models.UserSignUpModel;
import com.jms.system.models.UserOutBound;
import com.jms.system.repository.UserRepository;
import com.jms.system.services.UserService;

import reactor.core.publisher.Mono;

@Service
public class UserServiceImp implements UserService{

	@Autowired
	UserRepository repository;
	Logger logger=LoggerFactory.getLogger(UserServiceImp.class);

	@Override
	public Mono<UserOutBound> userSignUp(UserSignUpModel userInBound) {
		return Mono.just(Mono.just(userInBound)).flatMap(x->{
			ModelMapper mapper=new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			UserEntityModel user=mapper.map(userInBound, UserEntityModel.class);
			user.setUserId(UUID.randomUUID().toString());
			user.setPassword("{noop}".concat(user.getPassword()));
			return repository.save(user).map(y->{
				return mapper.map(y, UserOutBound.class);
			});
		});
	}
	
	
	public Mono<String> getUserId(String username) {
	return	repository.findUserId(username);
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return repository.findByUsername(username).map(x->{
			return new User(x.getUsername(), x.getPassword(), true, true, true, true, new ArrayList<>());
		});
	}
	
	
	

}
