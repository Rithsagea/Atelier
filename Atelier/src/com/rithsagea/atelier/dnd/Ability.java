package com.rithsagea.atelier.dnd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Ability {
	STRENGTH("str"),
	DEXTERITY("dex"),
	CONSTITUTION("con"),
	INTELLIGENCE("int"),
	WISDOM("wis"),
	CHARISMA("cha");
	
	private String label;
	
	private Ability(String label) {
		this.label = label;
	}
	
	@JsonValue
	public String getLabel() {
		return label;
	}
	
	@JsonCreator
	public Ability forLabel(String label) {
		for(Ability a : values()) if(a.getLabel().equals(label)) return a;
		return null;
	}
}
