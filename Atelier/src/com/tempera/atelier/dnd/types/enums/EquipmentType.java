package com.tempera.atelier.dnd.types.enums;

import com.tempera.util.WordUtil;

public enum EquipmentType implements Proficiency {
	LIGHT_ARMOR,
	
	WEAPONS,
	SIMPLE_WEAPONS,
	MARTIAL_WEAPON,
	MELEE_WEAPON,
	
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
