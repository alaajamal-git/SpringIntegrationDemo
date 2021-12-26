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
public class UserSignUpModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1766731669095999258L;
	private String username;
	private String password;
	private int age;
}
