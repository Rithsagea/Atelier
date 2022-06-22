package com.atelier.dnd.types.enums;

import java.util.HashMap;
import java.util.Map;

import com.atelier.AtelierLanguageManager;

public enum Ability implements Proficiency {
	STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA;

	private static Map<String, Ability> labelMap;
	static {
		labelMap = new HashMap<>();
		for (Ability ability : Ability.values()) {
			labelMap.put(ability.toString(), ability);
		}
	}

	public static Ability fromString(String str) {
		return labelMap.get(str);
	}

	private final String name;
	
	private Ability() {
		name = AtelierLanguageManager.getInstance().get(this, "name");
	}
	
	@Override
	public String toString() {
		return name;
	}
}
