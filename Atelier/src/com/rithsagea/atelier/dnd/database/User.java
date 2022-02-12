package com.rithsagea.atelier.dnd.database;

import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;

public class User {
	
	private final long id;
	
	private String name;
	private UUID sheetId;
	
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
	
	public UUID getSheetId() {
		return sheetId;
	}
	
	//MUTATORS
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSheetId(UUID id) {
		sheetId = id;
	}
}
