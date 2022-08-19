package com.atelier.dnd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AtelierCharacter {
	
	@JsonProperty("_id")
	private UUID id;
	private String name = "";
	
	public AtelierCharacter() {
		id = UUID.randomUUID();
	}
	
	public AtelierCharacter(UUID id) {
		this.id = id;
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
	
	@Override
	public String toString() {
		return String.format("%s [%s]", name, id);
	}
}
