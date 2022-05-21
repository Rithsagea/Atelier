package com.tempera.atelier.dnd.types.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Ability implements Proficiency {
	STRENGTH("str"),
	DEXTERITY("dex"),
	CONSTITUTION("con"),
	INTELLIGENCE("int"),
	WISDOM("wis"),
	CHARISMA("cha");
	
	private static Map<String, Ability> labelMap;
	static {
		labelMap = new HashMap<>();
		for(Ability ability : Ability.values()) {
			labelMap.put(ability.getLabel(), ability);
		}
	}
	
	public static Ability fromLabel(String label) {
		return labelMap.get(label);
	}
	
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
