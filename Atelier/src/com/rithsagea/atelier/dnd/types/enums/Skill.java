package com.rithsagea.atelier.dnd.types.enums;

public enum Skill implements Proficiency {
	ACROBATICS("acr", Ability.DEXTERITY),
	ANIMAL_HANDLING("ani", Ability.WISDOM),
	ARCANA("arc", Ability.INTELLIGENCE),
	ATHLETICS("ath", Ability.STRENGTH),
	DECEPTION("dec", Ability.CHARISMA),
	HISTORY("his", Ability.INTELLIGENCE),
	INSIGHT("ins", Ability.WISDOM),
	INTIMIDATION("int", Ability.CHARISMA),
	INVESTIGATION("inv", Ability.INTELLIGENCE),
	MEDICINE("med", Ability.WISDOM),
	NATURE("nat", Ability.INTELLIGENCE),
	PERCEPTION("prc",Ability.WISDOM),
	PERFORMANCE("prf",Ability.CHARISMA),
	PERSUASION("prs",Ability.CHARISMA),
	RELIGION("rel", Ability.INTELLIGENCE),
	SLEIGHT_OF_HAND("soh", Ability.DEXTERITY),
	STEALTH("ste", Ability.DEXTERITY),
	SURVIVAL("sur", Ability.WISDOM);
	
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
}
