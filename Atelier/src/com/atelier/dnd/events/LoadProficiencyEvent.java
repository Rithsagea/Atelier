package com.atelier.dnd.events;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.atelier.dnd.events.LoadEvent.LoadSheetEvent;
import com.atelier.dnd.types.Sheet;
import com.atelier.dnd.types.enums.Ability;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Skill;

public class LoadProficiencyEvent<E> extends LoadSheetEvent {
	public LoadProficiencyEvent(Sheet sheet) {
		super(sheet);
	}

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

	public static class LoadSavingProficiencyEvent extends LoadProficiencyEvent<Ability> {
		public LoadSavingProficiencyEvent(Sheet sheet) { super(sheet); }
	}

	public static class LoadSkillProficiencyEvent extends LoadProficiencyEvent<Skill> {
		public LoadSkillProficiencyEvent(Sheet sheet) { super(sheet); }
	}

	public static class LoadEquipmentProficiencyEvent extends LoadProficiencyEvent<EquipmentType> {
		public LoadEquipmentProficiencyEvent(Sheet sheet) { super(sheet); }
	}
}
