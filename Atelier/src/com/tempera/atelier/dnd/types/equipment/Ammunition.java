package com.tempera.atelier.dnd.types.equipment;

import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;

public class Ammunition extends BaseItem {

	public Ammunition(String name, String description, String source,
		Price price, double weight) {
		super(name, description, source, price, weight);
		addCategories(EquipmentType.AMMUNITION);
	}

	@Override
	public String getType() {
		return "ammunition";
	}

}
