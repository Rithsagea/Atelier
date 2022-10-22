package com.atelier.database;

import com.atelier.dnd.character.CharacterClass.TestAttribute;
import com.atelier.dnd.character.CharacterClass.TestClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

public class Types {
	public static void registerTypes(ObjectMapper mapper) {
		registerClasses(mapper);
	}

	private static void registerClasses(ObjectMapper mapper) {
		mapper.registerSubtypes(
			new NamedType(TestClass.class, "test"),
			new NamedType(TestAttribute.class, "test"));
	}
}
