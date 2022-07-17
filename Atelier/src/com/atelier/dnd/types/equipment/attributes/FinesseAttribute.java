package com.atelier.dnd.types.equipment.attributes;

import com.atelier.database.IndexedItem;

@IndexedItem("finesse")
public class FinesseAttribute implements ItemAttribute {

	@Override
	public String getName() {
		return "Finesse";
	}

	@Override
	public String getDescription() {
		return "When making an attack with a finesse weapon, you use your choice of your Strength or Dexterity modifier for the attack and damage rolls. You must use the same modifier for both rolls.";
	}
}
