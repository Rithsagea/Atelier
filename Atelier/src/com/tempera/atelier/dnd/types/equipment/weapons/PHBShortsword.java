package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.IndexedItem;
import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.EquipmentCategory;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.equipment.Weapon;

@IndexedItem("phb-shortsword")
public class PHBShortsword extends Weapon {
	public PHBShortsword() {
		super("shortsword", null,
				"Source: PHB, page 149. Available in the SRD and the Basic Rules.",
				new Price(2, Currency.GOLD), 2);
		
		addCategories(EquipmentCategory.MARTIAL_WEAPON, EquipmentCategory.MELEE_WEAPON);
	}
}
