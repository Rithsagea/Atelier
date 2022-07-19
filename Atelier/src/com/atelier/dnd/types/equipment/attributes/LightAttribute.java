package com.atelier.dnd.types.equipment.attributes;

import com.atelier.database.Subtype;

@Subtype("light")
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
