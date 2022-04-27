package com.tempera.atelier.dnd.types.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

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
	
	
	public String getLabel() {
		return label;
	}
	
	 //TODO delete this when people are done formatting older versions
	@JsonCreator
	public static Ability creator(String label) {
		try {
			return valueOf(label);
		} catch(IllegalArgumentException e) {
			for(Ability a : values())
				if(a.getLabel().equals(label))
					return a;
		}
		
		return null;
	}
}
