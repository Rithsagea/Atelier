package com.tempera.atelier.dnd.types.enums;

import com.tempera.util.WordUtil;

public enum EquipmentType implements Proficiency {
	ARMOR,
	LIGHT_ARMOR,
	
	
	WEAPON,
	SIMPLE_WEAPON,
	MARTIAL_WEAPON,
	MELEE_WEAPON,
	
	RANGE,
	HAND_CROSSBOW,
	LONGSWORD,
	RAPIER,
	SHORTSWORD,
	
	THIEVES_TOOLS;
	
	@Override
	public String toString() {
		return WordUtil.capitalize(name().replace("_", " "));
	}
}
