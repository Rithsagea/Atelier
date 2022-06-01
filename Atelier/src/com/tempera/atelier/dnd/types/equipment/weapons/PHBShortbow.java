package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.equipment.Weapon;

@IndexedItem("phb-shortbow")
public class PHBShortbow extends Weapon{

	public PHBShortbow(){
		super("Shortbow", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(25, Currency.GOLD), 2);
		addCategories(EquipmentType.RANGE, EquipmentType.WEAPON, EquipmentType.SIMPLE_WEAPON);
	}

}
