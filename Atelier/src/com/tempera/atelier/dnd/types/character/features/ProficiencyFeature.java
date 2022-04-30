package com.tempera.atelier.dnd.types.character.features;

import java.util.HashSet;
import java.util.Set;

import com.rithsagea.util.event.EventHandler;
import com.tempera.atelier.dnd.events.LoadProficiencyEvent.LoadEquipmentProficiencyEvent;
import com.tempera.atelier.dnd.events.LoadProficiencyEvent.LoadSavingProficiencyEvent;
import com.tempera.atelier.dnd.events.LoadProficiencyEvent.LoadSkillProficiencyEvent;
import com.tempera.atelier.dnd.types.character.Attribute;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Equipment;
import com.tempera.atelier.dnd.types.enums.Proficiency;
import com.tempera.atelier.dnd.types.enums.Skill;

public class ProficiencyFeature implements Attribute {
	private Set<Ability> savingProficiencies;
	private Set<Skill> skillProficiencies;
	private Set<Equipment> equipmentProficiencies;
	
	public ProficiencyFeature() {
		savingProficiencies = new HashSet<>();
		skillProficiencies = new HashSet<>();
		equipmentProficiencies = new HashSet<>();
	}
	
	public ProficiencyFeature(Proficiency... proficiencies) {
		this();
	
		for(Proficiency p : proficiencies) {
			if(p instanceof Ability) savingProficiencies.add((Ability) p);
			if(p instanceof Skill) skillProficiencies.add((Skill) p);
			if(p instanceof Equipment) equipmentProficiencies.add((Equipment) p);
		}
	}
	
	@EventHandler
	private void onLoadSavingProficiency(LoadSavingProficiencyEvent e) {
		for(Ability ability : savingProficiencies) e.addProficiency(ability);
	}
	
	@EventHandler
	private void onLoadSkillProficiency(LoadSkillProficiencyEvent e) {
		for(Skill skill : skillProficiencies) e.addProficiency(skill);
	}
	
	@EventHandler
	private void onLoadEquipmentProficiency(LoadEquipmentProficiencyEvent e) {
		for(Equipment equip : equipmentProficiencies) e.addProficiency(equip);
	}
}
