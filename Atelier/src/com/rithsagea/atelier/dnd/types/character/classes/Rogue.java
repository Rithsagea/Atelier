package com.rithsagea.atelier.dnd.types.character.classes;

import com.rithsagea.atelier.dnd.types.character.CharacterClass;
import com.rithsagea.atelier.dnd.types.enums.Ability;
import com.rithsagea.atelier.dnd.types.enums.Equipment;

public class Rogue extends CharacterClass {
	
	public Rogue() {
		addProficiencies(
				Equipment.LIGHT_ARMOR,
				Equipment.SIMPLE_WEAPONS,
				Equipment.HAND_CROSSBOWS,
				Equipment.LONGSWORDS,
				Equipment.RAPIERS,
				Equipment.SHORTSWORDS,
				Equipment.THIEVES_TOOLS,
				
				Ability.DEXTERITY,
				Ability.INTELLIGENCE);
	}
	
	@Override
	public String getName() {
		return "Rogue";
	}
}
