package com.rithsagea.phb.classes.rogue;

import java.util.LinkedHashMap;
import java.util.Map;

import com.atelier.dnd.character.CharacterClass;
import com.atelier.dnd.character.ClassFeature;

public class Rogue extends CharacterClass {
	
	@Override
	protected Map<String, ClassFeature> getFeatures(int level) {
		Map<String, ClassFeature> map = new LinkedHashMap<>();

		switch(level) {
			case 1:
				map.put("1-expertise", new RogueExpertiseFeature());
				map.put("1-sneak-attack", new SneakAttackFeature());
				map.put("1-thieves-cant", new ThievesCantFeature());
				break;
		}

		return map;
	}
}
