package com.rithsagea.phb.classes.rogue;

import com.atelier.dnd.character.CharacterClass;

public class Rogue extends CharacterClass {
	
	@Override
	public void init() {
		registerFeature(1, "1-expertise", new RogueExpertiseFeature());
		registerFeature(1, "1-sneak-attack", new SneakAttackFeature());
		registerFeature(1, "1-thieves-cant", new ThievesCantFeature());
	}
}
