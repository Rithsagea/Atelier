package com.atelier.dnd.types.equipment.ammunition;

import com.atelier.database.Subtype;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Ammunition;

@Subtype("phb-arrow")
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
