package com.tempera.atelier.dnd.types.equipment.attributes;

import com.tempera.atelier.dnd.types.IndexedItem;

@IndexedItem("loading")
public class LoadingAttribute implements ItemAttribute {

	@Override
	public String getName() {
		return "Loading";
	}

	@Override
	public String getDescription() {
		return "Because of the time required to load this weapon, you can fire only one piece of ammunition from it when you use an action, bonus action, or reaction to fire it, regardless of the number of attacks you can normally make.";
	}
	
}
