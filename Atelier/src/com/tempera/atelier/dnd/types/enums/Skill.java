package com.tempera.atelier.dnd.types.enums;

import java.util.HashMap;
import java.util.Map;

import com.tempera.util.WordUtil;

public enum Skill implements Proficiency {
	ACROBATICS("acr", Ability.DEXTERITY),
	ANIMAL_HANDLING("ani", Ability.WISDOM), ARCANA("arc", Ability.INTELLIGENCE),
	ATHLETICS("ath", Ability.STRENGTH), DECEPTION("dec", Ability.CHARISMA),
	HISTORY("his", Ability.INTELLIGENCE), INSIGHT("ins", Ability.WISDOM),
	INTIMIDATION("itm", Ability.CHARISMA),
	INVESTIGATION("inv", Ability.INTELLIGENCE), MEDICINE("med", Ability.WISDOM),
	NATURE("nat", Ability.INTELLIGENCE), PERCEPTION("prc", Ability.WISDOM),
	PERFORMANCE("prf", Ability.CHARISMA), PERSUASION("prs", Ability.CHARISMA),
	RELIGION("rel", Ability.INTELLIGENCE),
	SLEIGHT_OF_HAND("soh", Ability.DEXTERITY),
	STEALTH("ste", Ability.DEXTERITY), SURVIVAL("sur", Ability.WISDOM);

	private static Map<String, Skill> labelMap;
	static {
		labelMap = new HashMap<>();
		for (Skill skill : Skill.values()) {
			labelMap.put(skill.toString(), skill);
//			labelMap.put(skill.getLabel(), skill);
		}
	}

	public static Skill fromString(String str) {
		return labelMap.get(str);
	}

	private final String label;
	private final Ability ability;

	private Skill(String label, Ability ability) {
		this.label = label;
		this.ability = ability;
	}

	public String getLabel() {
		return label;
	}

	public Ability getAbility() {
		return ability;
	}

	@Override
	public String toString() {
		return WordUtil.capitalize(name().replace("_", " "));
	}
}
