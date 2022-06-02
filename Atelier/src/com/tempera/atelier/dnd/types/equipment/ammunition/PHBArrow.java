package com.tempera.atelier.dnd.types.equipment.ammunition;

import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.equipment.Ammunition;

public class PHBArrow extends Ammunition {

	public PHBArrow(String name, String description, String source, Price price,
		int weight) {
		super("Arrow", null,
			"PHB, page 150. Available in the SRD and the Basic Rules.",
			new Price(5, Currency.COPPER), 1);
		// TODO: Arrows 20 times as heavy
	}

}
