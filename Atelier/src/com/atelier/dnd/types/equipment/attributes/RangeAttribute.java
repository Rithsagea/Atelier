package com.atelier.dnd.types.equipment.attributes;

import com.atelier.database.Subtype;

@Subtype("range")
public class RangeAttribute implements ItemAttribute {

	@Override
	public String getName() {
		return "Range";
	}

	@Override
	public String getDescription() {
		return "A weapon that can be used to make a ranged attack has a range shown in parentheses after the ammunition or thrown property. The range lists two numbers. The first is the weapon's normal range in feet, and the second indicates the weapon's maximum range. When attacking a target beyond normal range, you have disadvantage on the attack roll. You can't attack a target beyond the weapon's long range.";
	}

}
