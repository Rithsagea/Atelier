package com.atelier.database;

import com.atelier.dnd.CharacterClass.TestClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

public class Types {
	public static void registerTypes(ObjectMapper mapper) {
		registerClasses(mapper);
	}

	private static void registerClasses(ObjectMapper mapper) {
		mapper.registerSubtypes(new NamedType(TestClass.class, "test-class"));
	}
}
