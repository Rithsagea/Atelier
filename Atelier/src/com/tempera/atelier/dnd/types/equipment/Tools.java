package com.tempera.atelier.dnd.types.equipment;

import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;

public class Tools extends BaseItem {

	public Tools(String name, String description, String source, Price price,
		double weight) {
		super(name, description, source, price, weight);
		addCategories(EquipmentType.TOOLS);
	}

	@Override
	public String getType() {
		return "Tools";
	}

}
