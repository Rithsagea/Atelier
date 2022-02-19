package com.rithsagea.atelier.dnd;

import java.util.UUID;

import org.mongojack.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rithsagea.atelier.dnd.types.spread.AbilitySpread;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sheet {
	
	@Id
	private final UUID id;
	
	private String name;
	private AbilitySpread baseScores;
	
	@JsonCreator
	public Sheet(@Id UUID id) {
		this.id = id;
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
	
	public AbilitySpread getBaseScores() {
		return baseScores;
	}
	
	public int getAbilityScore(Ability ability) {
		return baseScores.getBaseScore(ability);
	}
	
	//MUTATORS
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setBaseScores(AbilitySpread spread) {
		baseScores = spread;
	}
}
