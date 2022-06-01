package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.IndexedItem;
import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.equipment.Weapon;

@IndexedItem("phb-rapier")
public class PHBRapier extends Weapon {

	public PHBRapier() {
		super("Rapier", null,
				"PHB, page 149. Available in the SRD and the Basic Rules.",
				new Price(25, Currency.GOLD), 2);
		
		addCategories(EquipmentType.MARTIAL_WEAPON, EquipmentType.MELEE_WEAPON);
	}
}
