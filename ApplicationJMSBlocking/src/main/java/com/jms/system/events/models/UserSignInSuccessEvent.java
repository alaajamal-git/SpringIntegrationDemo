package com.jms.system.events.models;

import java.io.Serializable;
import java.util.Date;

import org.springframework.security.core.userdetails.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSignInSuccessEvent extends EventModel implements Serializable{

	private static final long serialVersionUID = -1509494751885916306L;
	private EventType type;
	private Date date;
	private User user;

}
