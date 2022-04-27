package com.tempera.atelier.dnd.events;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.rithsagea.util.event.Event;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Equipment;
import com.tempera.atelier.dnd.types.enums.Skill;

public class LoadProficiencyEvent<E> implements Event {
	private Set<E> proficiencies = new HashSet<>();
	
	public Set<E> getProficiencies() {
		return Collections.unmodifiableSet(proficiencies);
	}
	
	public boolean hasProficiency(E proficiency) {
		return proficiencies.contains(proficiency);
	}
	
	public void addProficiency(E proficiency) {
		proficiencies.add(proficiency);
	}
	
	public void removeProficiency(E proficiency) {
		proficiencies.remove(proficiency);
	}
	
	public static class LoadSavingProficiencyEvent extends LoadProficiencyEvent<Ability> { }
	public static class LoadSkillProficiencyEvent extends LoadProficiencyEvent<Skill> { }
	public static class LoadEquipmentProficiencyEvent extends LoadProficiencyEvent<Equipment> { }
}
