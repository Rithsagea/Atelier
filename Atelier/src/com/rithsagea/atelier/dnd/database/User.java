package com.rithsagea.atelier.dnd.database;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;

public class User {
	
	private final long id;
	
	private String name;
	
	@BsonCreator
	public User(@BsonId long id) {
		this.id = id;
	}
	
	//ACCESSORS
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	//MUTATORS
	
	public void setName(String name) {
		this.name = name;
	}
}
