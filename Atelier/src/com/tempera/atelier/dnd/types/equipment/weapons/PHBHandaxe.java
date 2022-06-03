package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.equipment.Weapon;

public class PHBHandaxe extends Weapon{

	public PHBHandaxe() {
		super("Handaxe", null, "PHB, page 149. Available in the SRD and the Basic Rules.",
			new Price(5, Currency.GOLD), 2);
		}

}
