package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.equipment.Weapon;

public class PHBLightCrossbow extends Weapon{

	public PHBLightCrossbow() {
		super("Light Crossbow", null, "PHB, page 149. Available in the SRD and the Basic Rules.",
			new Price(25, Currency.GOLD), 5);
	}

}
