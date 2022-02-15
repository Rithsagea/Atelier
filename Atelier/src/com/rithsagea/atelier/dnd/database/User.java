package com.rithsagea.atelier.dnd.database;

import java.util.UUID;

import org.mongojack.Id;

import com.fasterxml.jackson.annotation.JsonCreator;

public class User {
	
	@Id
	private final long id;
	
	private String name;
	private UUID sheetId;
	
	@JsonCreator
	public User(@Id long id) {
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
