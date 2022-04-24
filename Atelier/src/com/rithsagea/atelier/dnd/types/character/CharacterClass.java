package com.rithsagea.atelier.dnd.types.character;

import java.util.HashSet;
import java.util.Set;

import com.rithsagea.atelier.dnd.types.enums.Ability;
import com.rithsagea.atelier.dnd.types.enums.Equipment;
import com.rithsagea.atelier.dnd.types.enums.Proficiency;
import com.rithsagea.atelier.dnd.types.enums.Skill;

public abstract class CharacterClass implements AbstractClass {
	private int level = 1;
	
	private Set<Ability> savingProficiencies = new HashSet<>();
	private Set<Skill> skillProficiencies = new HashSet<>();
	private Set<Equipment> equipmentProficiencies = new HashSet<>();
	
	@Override
	public int getLevel() {
		return level;
	}
	
	@Override
	public boolean hasProficiency(Proficiency proficiency) {
		if(proficiency instanceof Ability) return savingProficiencies.contains(proficiency);
		if(proficiency instanceof Skill) return skillProficiencies.contains(proficiency);
		if(proficiency instanceof Equipment) return equipmentProficiencies.contains(proficiency);
		
		return false;
	}
	
	protected void addProficiency(Proficiency proficiency) {
		if(proficiency instanceof Ability) savingProficiencies.add((Ability) proficiency);
		if(proficiency instanceof Skill) skillProficiencies.add((Skill) proficiency);
		if(proficiency instanceof Equipment) equipmentProficiencies.add((Equipment) proficiency);
	}
	
	protected void addProficiencies(Proficiency...proficiencies) {
		for(Proficiency p : proficiencies)
			addProficiency(p);
	}
	
	
	@Override
	public String toString() {
		return getName() + " " + level;
	}
}
