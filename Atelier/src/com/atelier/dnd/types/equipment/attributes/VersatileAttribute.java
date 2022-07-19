package com.atelier.dnd.types.equipment.attributes;

import com.atelier.database.Subtype;

@Subtype("versatile")
public class VersatileAttribute implements ItemAttribute {

	@Override
	public String getName() {
		return "Versatile";
	}

	@Override
	public String getDescription() {
		return "This weapon can be used with one or two hands. A damage value in parentheses appears with the property—the damage when the weapon is used with two hands to make a melee attack.";
	}

}
