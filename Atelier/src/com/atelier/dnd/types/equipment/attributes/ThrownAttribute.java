package com.atelier.dnd.types.equipment.attributes;

import com.atelier.database.IndexedItem;

@IndexedItem("thrown")
public class ThrownAttribute implements ItemAttribute {

	@Override
	public String getName() {
		return "Thrown";
	}

	@Override
	public String getDescription() {
		return "If a weapon has the thrown property, you can throw the weapon to make a ranged attack. If the weapon is a melee weapon, you use the same ability modifier for that attack roll and damage roll that you would use for a melee attack with the weapon. For example, if you throw a handaxe, you use your Strength, but if you throw a dagger, you can use either your Strength or your Dexterity, since the dagger has the finesse property.";
	}
	
}
