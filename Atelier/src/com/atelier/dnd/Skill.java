package com.atelier.dnd;

import com.atelier.AtelierObject;

public enum Skill implements AtelierObject {
	ACROBATICS(Ability.DEXTERITY), ANIMAL_HANDLING(Ability.WISDOM), ARCANA(Ability.INTELLIGENCE),
	ATHLETICS(Ability.STRENGTH), DECEPTION(Ability.CHARISMA), HISTORY(Ability.INTELLIGENCE), INSIGHT(Ability.WISDOM),
	INTIMIDATION(Ability.CHARISMA), INVESTIGATION(Ability.INTELLIGENCE), MEDICINE(Ability.WISDOM),
	NATURE(Ability.INTELLIGENCE), PERCEPTION(Ability.WISDOM), PERFORMANCE(Ability.CHARISMA),
	PERSUASION(Ability.CHARISMA), RELIGION(Ability.INTELLIGENCE), SLEIGHT_OF_HAND(Ability.DEXTERITY),
	STEALTH(Ability.DEXTERITY), SURVIVAL(Ability.WISDOM);

	private final Ability ability;
	private final String name = getProperty("name");
	
	private Skill(Ability ability) {
		this.ability = ability;
	}
	
	public Ability getAbility() {
		return ability;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
