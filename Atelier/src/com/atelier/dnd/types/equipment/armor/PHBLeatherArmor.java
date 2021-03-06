package com.atelier.dnd.types.equipment.armor;

import com.atelier.database.Subtype;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Armor;

@Subtype("phb-leather-armor")
public class PHBLeatherArmor extends Armor {

	public PHBLeatherArmor() {
		super("Leather Armor",
			"The breastplate and shoulder protectors of this armor are made of leather "
				+ "that has been stiffened by being boiled in oil. The rest of the armor is "
				+ "made of softer and more flexible materials.",
			"PHB, page 144. Available in the SRD and the Basic Rules.",
			new Price(10, Currency.GOLD), 10);
	}

}
