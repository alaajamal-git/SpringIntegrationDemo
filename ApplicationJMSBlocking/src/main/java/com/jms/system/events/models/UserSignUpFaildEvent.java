package com.jms.system.events.models;

import java.io.Serializable;
import java.util.Date;
import com.jms.system.models.UserSignUpModel;
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
public class UserSignUpFaildEvent extends EventModel implements Serializable{

	private static final long serialVersionUID = -1509494751885916306L;
	private EventType type;
	private Date date;
	private UserSignUpModel user;
	private String Ip;

}
