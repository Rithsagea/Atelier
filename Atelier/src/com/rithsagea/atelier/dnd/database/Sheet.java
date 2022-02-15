package com.rithsagea.atelier.dnd.database;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.mongojack.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rithsagea.atelier.dnd.database.types.Ability;

public class Sheet {
	
	@Id
	private final UUID id;
	
	private String name;
	
	@JsonProperty
	private Map<String, Integer> abilityScores;
	
	@JsonCreator
	public Sheet(@Id UUID id) {
		this.id = id;
		
		this.abilityScores = new HashMap<>();
	}
	
	public Sheet() {
		this(UUID.randomUUID());
	}
	
	//ACCESSORS
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAbilityScore(Ability ability) {
		if(!abilityScores.containsKey(ability.toString()))
			abilityScores.put(ability.toString(), 0); // TODO default value;
		
		return abilityScores.get(ability.toString());
	}
	
	//MUTATORS
	
	public void setName(String name) {
		this.name = name;
	}
}
