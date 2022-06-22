package com.atelier.dnd.types.equipment;

import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;

public class AdventuringGear extends BaseItem {

	public AdventuringGear(String name, String description, String source,
		Price price, double weight) {
		super(name, description, source, price, weight);
		addCategories(EquipmentType.ADVENTURING_GEAR);
	}

	@Override
	public String getType() {
		return "Adventuring Gear";
	}

}
