package com.atelier.dnd.character;

import java.util.Collections;
import java.util.Map;

import com.atelier.database.AtelierType;

@AtelierType("null")
public class NullClass extends CharacterClass {

	@Override
	protected Map<String, ClassFeature> getFeatures(int level) {
		return Collections.emptyMap();
	}
	
}
