package com.atelier.dnd.types.equipment;

import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;

public class Weapon extends BaseItem {

	public Weapon(String name, String description, String source, Price price, double weight) {
		super(name, description, source, price, weight);

		addCategories(EquipmentType.WEAPON);
	}

	@Override
	public final String getType() {
		return "Weapon";
	}
}
