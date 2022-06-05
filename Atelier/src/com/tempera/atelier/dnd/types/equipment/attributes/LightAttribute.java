package com.tempera.atelier.dnd.types.equipment.attributes;

import com.tempera.atelier.dnd.types.IndexedItem;

@IndexedItem("light")
public class LightAttribute implements ItemAttribute {

	@Override
	public String getName() {
		return "Light";
	}

	@Override
	public String getDescription() {
		return "A light weapon is small and easy to handle, making it ideal for use when fighting with two weapons.";
	}
	
}
