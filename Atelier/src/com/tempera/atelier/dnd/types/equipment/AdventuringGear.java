package com.tempera.atelier.dnd.types.equipment;

import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;

public class AdventuringGear extends BaseItem {

	public AdventuringGear(String name, String description, String source,
		Price price, double weight) {
		super(name, description, source, price, weight);
		addCategories(EquipmentType.ADVENTURING_GEAR);
	}

	@Override
	public String getType() {
		return "adventuring gear";
	}

}
