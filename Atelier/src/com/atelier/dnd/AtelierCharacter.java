package com.atelier.dnd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AtelierCharacter {
	
	@JsonProperty("_id")
	private UUID id;
	private String name = "";
	
	private AbilitySpread abilitySpread = new AbilitySpread();
	
	public AtelierCharacter() {
		this(UUID.randomUUID());
	}
	
	public AtelierCharacter(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public AbilitySpread getAbilitySpread() {
		return abilitySpread;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("%s [%s]", name, id);
	}
}
