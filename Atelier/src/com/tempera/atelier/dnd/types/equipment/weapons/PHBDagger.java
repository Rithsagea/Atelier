package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.EquipmentCategory;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.equipment.Weapon;

public class PHBDagger extends Weapon{

	public PHBDagger() {
		super("Dagger", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(2, Currency.GOLD), 1);
		addCategories(EquipmentCategory.MARTIAL_WEAPON, EquipmentCategory.MELEE_WEAPON);

	}
	

}
