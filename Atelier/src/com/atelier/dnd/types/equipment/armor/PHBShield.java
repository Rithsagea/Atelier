package com.atelier.dnd.types.equipment.armor;

import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Armor;

public class PHBShield extends Armor{

	public PHBShield() {
		super("Shield", "A shield is made from wood or metal and is carried in one hand. Wielding a shield increases your Armor Class by 2. You can benefit from only one shield at a time.",
			"PHB, page 144. Available in the SRD and the Basic Rules.",
			new Price(10, Currency.GOLD), 6);
	}

}
