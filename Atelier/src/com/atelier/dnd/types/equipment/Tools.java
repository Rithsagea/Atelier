package com.atelier.dnd.types.equipment;

import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;

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
