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
public class UserOutBound implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 974585026563652195L;
	private String username;
	private String userId;

}
