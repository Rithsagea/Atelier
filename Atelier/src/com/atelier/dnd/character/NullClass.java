package com.atelier.dnd.character;

import java.util.Collections;
import java.util.Map;

public class NullClass extends CharacterClass {

	@Override
	protected Map<String, ClassFeature> getFeatures(int level) {
		return Collections.emptyMap();
	}
	
}
