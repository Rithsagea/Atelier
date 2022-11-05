package com.rithsagea.phb.classes.rogue;

import com.atelier.dnd.character.CharacterClass;
import com.atelier.dnd.character.ClassFeature;

public class Rogue extends CharacterClass {
	
	public static class TestAttribute extends ClassFeature {
		public String test = "foo";
	}
	
	@Override
	public void init() {
		registerAttribute(0, "0-test", new TestAttribute());

		registerAttribute(1, "1-expertise", new RogueExpertiseFeature());
		registerAttribute(1, "1-sneak-attack", new SneakAttackFeature());
		registerAttribute(1, "1-thieves-cant", new ThievesCantFeature());
	}
}
