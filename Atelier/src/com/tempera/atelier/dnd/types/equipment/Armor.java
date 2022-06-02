package com.tempera.atelier.dnd.types.equipment;

import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;

public abstract class Armor extends BaseItem {

	public Armor(String name, String description, String source, Price price,
		double weight) {
		super(name, description, source, price, weight);

		addCategories(EquipmentType.ARMOR);
	}

	@Override
	public final String getType() {
		return "armor";
	}
}
