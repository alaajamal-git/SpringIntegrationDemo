package com.jms.system.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSignInModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5695668558881492949L;
	private String username;
	private String password;
}
