package com.tempera.atelier.dnd.types.equipment;

import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.enums.Currency.Price;

public class Tools extends BaseItem {

	public Tools(String name, String description, String source, Price price,
		int weight) {
		super(name, description, source, price, weight);
		addCategories(EquipmentType.TOOLS);
	}

	@Override
	public String getType() {
		return "tools";
	}

}
