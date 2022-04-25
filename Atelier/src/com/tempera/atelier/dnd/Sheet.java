package com.tempera.atelier.dnd;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mongojack.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tempera.atelier.dnd.types.character.AbstractClass;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.spread.AbilitySpread;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sheet {
	
	@Id
	private final UUID id;
	
	private String name;
	private AbilitySpread baseScores;
	
	private List<AbstractClass> classes;
	
	@JsonCreator
	public Sheet(@Id UUID id) {
		this.id = id;
		
		classes = new ArrayList<>();
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
	
	public int getAbilityModifier(Ability ability) {
		return (baseScores.getBaseScore(ability)-10)/2;
	}
	
	public List<AbstractClass> getClasses() {
		return classes;
	}
	
	//MUTATORS
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setBaseScores(AbilitySpread spread) {
		baseScores = spread;
	}
	
	public void addClass(AbstractClass clazz) {
		classes.add(clazz);
	}
}
