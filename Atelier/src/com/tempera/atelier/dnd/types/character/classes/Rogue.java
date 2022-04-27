package com.tempera.atelier.dnd.types.character.classes;

import com.tempera.atelier.dnd.types.character.CharacterClass;
import com.tempera.atelier.dnd.types.character.attributes.ProficiencyFeature;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Equipment;

public class Rogue extends CharacterClass {
	
	public Rogue() {
		addAttribute("proficiency",
				new ProficiencyFeature(
					Equipment.LIGHT_ARMOR, Equipment.SIMPLE_WEAPONS, Equipment.HAND_CROSSBOWS,
					Equipment.LONGSWORDS, Equipment.RAPIERS, Equipment.SHORTSWORDS,
					Equipment.THIEVES_TOOLS,
					
					Ability.DEXTERITY, Ability.INTELLIGENCE));
	}
	
	@Override
	public String getName() {
		return "Rogue";
	}
}
