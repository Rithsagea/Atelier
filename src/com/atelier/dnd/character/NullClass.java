package com.atelier.dnd.character;

import java.util.Collections;
import java.util.Map;

import com.atelier.database.AtelierType;
import com.atelier.dnd.character.attributes.CharacterClass;
import com.atelier.dnd.character.attributes.ClassFeature;

@AtelierType("null")
public class NullClass extends CharacterClass {

	@Override
	protected Map<String, ClassFeature> getFeatures(int level) {
		return Collections.emptyMap();
	}
	
}
