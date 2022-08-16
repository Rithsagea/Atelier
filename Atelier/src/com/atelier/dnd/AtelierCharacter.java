package com.atelier.dnd;

import java.util.UUID;

import com.atelier.database.annotations.Id;

public class AtelierCharacter {
	
	@Id
	private UUID id;
	private String name = "";
	
	public AtelierCharacter() {
		id = UUID.randomUUID();
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
