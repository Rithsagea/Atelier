package com.tempera.atelier.dnd.types.enums;

import com.tempera.util.WordUtil;

public enum Equipment implements Proficiency {
	LIGHT_ARMOR,
	
	SIMPLE_WEAPONS,
	HAND_CROSSBOWS,
	LONGSWORDS,
	RAPIERS,
	SHORTSWORDS,
	
	THIEVES_TOOLS;
	
	@Override
	public String toString() {
		return WordUtil.capitalize(name().replace("_", " "));
	}
}
