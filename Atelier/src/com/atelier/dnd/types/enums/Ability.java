package com.atelier.dnd.types.enums;

import java.util.HashMap;
import java.util.Map;

import com.atelier.util.WordUtil;

public enum Ability implements Proficiency {
	STRENGTH("str"), DEXTERITY("dex"), CONSTITUTION("con"), INTELLIGENCE("int"),
	WISDOM("wis"), CHARISMA("cha");

	private static Map<String, Ability> labelMap;
	static {
		labelMap = new HashMap<>();
		for (Ability ability : Ability.values()) {
//			labelMap.put(ability.getLabel(), ability);
			labelMap.put(ability.toString(), ability);
		}
	}

	public static Ability fromString(String str) {
		return labelMap.get(str);
	}

	private String label;

	private Ability(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return WordUtil.capitalize(name().replace("_", " "));
	}
}
