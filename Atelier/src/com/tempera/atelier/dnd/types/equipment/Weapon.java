package com.tempera.atelier.dnd.types.equipment;

import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;

public abstract class Weapon extends BaseItem {

	public Weapon(String name, String description, String source, Price price,
		double weight) {
		super(name, description, source, price, weight);

		addCategories(EquipmentType.WEAPON);
	}

	@Override
	public final String getType() {
		return "weapon";
	}
}
