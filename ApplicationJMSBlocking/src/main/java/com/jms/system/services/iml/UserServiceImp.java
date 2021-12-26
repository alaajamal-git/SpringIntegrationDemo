package com.jms.system.services.iml;

import java.util.ArrayList;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.jms.system.models.UserEntityModel;
import com.jms.system.models.UserSignUpModel;
import com.jms.system.models.UserOutBound;
import com.jms.system.repository.UserRepository;
import com.jms.system.services.UserService;

@Service
public class UserServiceImp implements UserService{

	@Autowired
	UserRepository repository;
	

	@Override
	public UserOutBound userSignUp(UserSignUpModel userInBound) {
		ModelMapper mapper=new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntityModel user=mapper.map(userInBound, UserEntityModel.class);
		user.setUserId(UUID.randomUUID().toString());
		user.setPassword("{noop}".concat(user.getPassword()));
		UserOutBound userOutBound =mapper.map(repository.save(user), UserOutBound.class);
		return userOutBound;
	}
	
	//used by web security configurer to load user details
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntityModel user = repository.findByUsername(username);
		if(user == null) throw new UsernameNotFoundException(username);
		return new User(user.getUsername(), user.getPassword(), true, true, true, true, new ArrayList<>());
	}
	
	public String getUserId(String username) {
		
		return repository.findByUsername(username).getUserId();
		
	}

}
