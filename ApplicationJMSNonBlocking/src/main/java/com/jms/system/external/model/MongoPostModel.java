package com.jms.system.external.model;

import java.io.Serializable;

import com.jms.system.events.models.EventModel;

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
public class MongoPostModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2627458901227387072L;
	private String dataSource;
	private String database;
	private String collection;
	private EventModel document;

}
