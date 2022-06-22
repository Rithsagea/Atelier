package com.atelier.dnd.types.enums;

import java.util.HashMap;
import java.util.Map;

import com.atelier.AtelierLanguageManager;

public enum Skill implements Proficiency {
	ACROBATICS(Ability.DEXTERITY), ANIMAL_HANDLING(Ability.WISDOM), ARCANA(Ability.INTELLIGENCE),
	ATHLETICS(Ability.STRENGTH), DECEPTION(Ability.CHARISMA), HISTORY(Ability.INTELLIGENCE), INSIGHT(Ability.WISDOM),
	INTIMIDATION(Ability.CHARISMA), INVESTIGATION(Ability.INTELLIGENCE), MEDICINE(Ability.WISDOM),
	NATURE(Ability.INTELLIGENCE), PERCEPTION(Ability.WISDOM), PERFORMANCE(Ability.CHARISMA),
	PERSUASION(Ability.CHARISMA), RELIGION(Ability.INTELLIGENCE), SLEIGHT_OF_HAND(Ability.DEXTERITY),
	STEALTH(Ability.DEXTERITY), SURVIVAL(Ability.WISDOM);

	private static Map<String, Skill> labelMap;
	static {
		labelMap = new HashMap<>();
		for (Skill skill : Skill.values()) {
			labelMap.put(skill.toString(), skill);
		}
	}

	public static Skill fromString(String str) {
		return labelMap.get(str);
	}

	private final Ability ability;
	private final String name;
	
	private Skill(Ability ability) {
		this.ability = ability;
		
		name = AtelierLanguageManager.getInstance().get(this, "name");
	}

	public Ability getAbility() {
		return ability;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
