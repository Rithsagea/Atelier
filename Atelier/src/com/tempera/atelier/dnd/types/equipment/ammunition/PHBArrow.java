package com.tempera.atelier.dnd.types.equipment.ammunition;

import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.equipment.Ammunition;

@IndexedItem("phb-arrow")
public class PHBArrow extends Ammunition {

	public PHBArrow() {
		super("Arrow", null,
			"PHB, page 150. Available in the SRD and the Basic Rules.",
			new Price(5, Currency.COPPER), 0.05);
	}

	public PHBArrow(int amount) {
		this();

		setAmount(amount);
	}

}
