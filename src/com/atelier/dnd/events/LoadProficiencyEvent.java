package com.atelier.dnd.events;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.atelier.dnd.Ability;
import com.atelier.dnd.Skill;
import com.atelier.dnd.character.AtelierCharacter;
import com.atelier.dnd.events.LoadEvent.LoadCharacterEvent;

public class LoadProficiencyEvent<T extends Enum<T>> extends LoadCharacterEvent {

	private Set<T> proficiencies = new HashSet<>();
	
	public LoadProficiencyEvent(AtelierCharacter character) {
		super(character);
	}
	
	public Set<T> getProficiencies() {
		return Collections.unmodifiableSet(proficiencies);
	}

	public boolean hasProficiency(T proficiency) {
		return proficiencies.contains(proficiency);
	}

	public void addProficiency(T proficiency) {
		proficiencies.add(proficiency);
	}

	public void removeProficiency(T proficiency) {
		proficiencies.remove(proficiency);
	}
	
	public static class LoadSavingProficiencyEvent extends LoadProficiencyEvent<Ability> {
		public LoadSavingProficiencyEvent(AtelierCharacter character) { super(character); }
	}

	public static class LoadSkillProficiencyEvent extends LoadProficiencyEvent<Skill> {
		public LoadSkillProficiencyEvent(AtelierCharacter character) { super(character); }
	}

}
