package com.tempera.atelier.dnd.types.character.classes;

import com.tempera.atelier.dnd.IndexedItem;
import com.tempera.atelier.dnd.types.character.CharacterClass;
import com.tempera.atelier.dnd.types.character.features.HitPointFeature;
import com.tempera.atelier.dnd.types.character.features.ProficiencyFeature;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Equipment;

@IndexedItem("rogue")
public class Rogue extends CharacterClass {
	
	public Rogue() {
		addAttribute("proficiency", new ProficiencyFeature(
					Equipment.LIGHT_ARMOR, Equipment.SIMPLE_WEAPONS, Equipment.HAND_CROSSBOWS,
					Equipment.LONGSWORDS, Equipment.RAPIERS, Equipment.SHORTSWORDS,
					Equipment.THIEVES_TOOLS,
					
					Ability.DEXTERITY, Ability.INTELLIGENCE));
		
		addAttribute("hit-points", new HitPointFeature(8));
	}
	
	@Override
	public String getName() {
		return "Rogue";
	}
}
