package com.rithsagea.phb.classes.rogue;

import com.atelier.dnd.character.CharacterClass;
import com.atelier.dnd.character.ClassFeature;

public class Rogue extends CharacterClass {
	
	public static class TestAttribute extends ClassFeature {
		public String test = "foo";
	}
	
	@Override
	public void init() {
		registerFeature(0, "0-test", new TestAttribute());

		registerFeature(1, "1-expertise", new RogueExpertiseFeature());
		registerFeature(1, "1-sneak-attack", new SneakAttackFeature());
		registerFeature(1, "1-thieves-cant", new ThievesCantFeature());
	}
}
