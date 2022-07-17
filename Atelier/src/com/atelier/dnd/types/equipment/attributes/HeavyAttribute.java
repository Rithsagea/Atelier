package com.atelier.dnd.types.equipment.attributes;

import com.atelier.database.IndexedItem;

@IndexedItem("heavy")
public class HeavyAttribute implements ItemAttribute {

	@Override
	public String getName() {
		return "Heavy";
	}

	@Override
	public String getDescription() {
		return "Creatures that are Small or Tiny have disadvantage on attack rolls with heavy weapons. A heavy weapon's size and bulk make it too large for a Small or Tiny creature to use effectively.";
	}
	
}
