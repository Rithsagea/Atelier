package com.atelier.dnd.types.equipment;

import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;

public class Armor extends BaseItem {

	public Armor(String name, String description, String source, Price price,
		double weight) {
		super(name, description, source, price, weight);

		addCategories(EquipmentType.ARMOR);
	}

	@Override
	public final String getType() {
		return "Armor";
	}
}
