package com.jms.system.models;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
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
@Table
public class UserEntityModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8614321967769396092L;
	
	@Id
	private long id;
	private String username;
	private String password;
	private String userId;

}
