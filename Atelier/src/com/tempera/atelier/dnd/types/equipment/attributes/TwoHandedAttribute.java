package com.tempera.atelier.dnd.types.equipment.attributes;

import com.tempera.atelier.dnd.types.IndexedItem;

@IndexedItem("two-handed")
public class TwoHandedAttribute implements ItemAttribute {

	@Override
	public String getName() {
		return "Two-Handed";
	}

	@Override
	public String getDescription() {
		return "This weapon requires two hands to use. This property is relevant only when you attack with the weapon, not when you simply hold it.";
	}
	
}
