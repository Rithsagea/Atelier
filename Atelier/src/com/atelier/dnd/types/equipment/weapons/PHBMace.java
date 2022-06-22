package com.atelier.dnd.types.equipment.weapons;

import com.atelier.dnd.types.IndexedItem;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;

@IndexedItem("phb-mace")
public class PHBMace extends Weapon {

	public PHBMace() {
		super("Mace", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(5, Currency.GOLD), 4);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
	}

}
