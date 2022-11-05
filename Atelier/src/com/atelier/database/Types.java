package com.atelier.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.rithsagea.phb.classes.rogue.Rogue;
import com.rithsagea.phb.classes.rogue.RogueExpertiseFeature;
import com.rithsagea.phb.classes.rogue.SneakAttackFeature;
import com.rithsagea.phb.classes.rogue.ThievesCantFeature;
import com.rithsagea.phb.classes.rogue.Rogue.TestAttribute;

public class Types {
	public static void registerTypes(ObjectMapper mapper) {
		registerClasses(mapper);
	}

	private static void registerClasses(ObjectMapper mapper) {
		mapper.registerSubtypes(
			new NamedType(Rogue.class, "rogue"),
			new NamedType(TestAttribute.class, "test"),
			new NamedType(RogueExpertiseFeature.class, "rogue-expertise"),
			new NamedType(SneakAttackFeature.class, "sneak-attack"),
			new NamedType(ThievesCantFeature.class, "thieves-cant"));
	}
}
