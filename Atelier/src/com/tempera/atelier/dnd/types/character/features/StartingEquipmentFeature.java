package com.tempera.atelier.dnd.types.character.features;

import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.character.Attribute;

@IndexedItem("feature-starting-equipment")
public class StartingEquipmentFeature implements Attribute {

	@Override
	public String getName() {
		return "Starting Equipment";
	}

	@Override
	public Menu getMenu() {
		return null;
	}

}
