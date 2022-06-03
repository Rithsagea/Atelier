package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.equipment.Weapon;

public class PHBLongbow extends Weapon{

	public PHBLongbow() {
		super("Longbow", null, "PHB, page 149. Available in the SRD and the Basic Rules.",
			new Price(50, Currency.GOLD), 2);
	}

}
