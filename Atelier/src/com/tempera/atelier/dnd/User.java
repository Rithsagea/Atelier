package com.tempera.atelier.dnd;

import java.util.UUID;

import org.mongojack.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tempera.atelier.discord.commands.PermissionLevel;

public class User {
	
	@Id
	private final long id;
	
	private String name;
	private UUID sheetId;
	
	private PermissionLevel level;
	
	@JsonCreator
	public User(@Id long id) {
		this.id = id;
		
		level = PermissionLevel.USER;
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
	
	public PermissionLevel getLevel() {
		return level;
	}
	
	//MUTATORS
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSheetId(UUID id) {
		sheetId = id;
	}
	
	public void setLevel(PermissionLevel level) {
		this.level = level;
	}
}
