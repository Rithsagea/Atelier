package com.atelier.dnd.types.equipment.weapons;

import com.atelier.dnd.types.IndexedItem;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.ThrownAttribute;

@IndexedItem("phb-javelin")
public class PHBJavelin extends Weapon {

	public PHBJavelin() {
		super("Javelin", null, "PHB, page 149. Available in the SRD and the Basic Rules.",
				new Price(5, Currency.SILVER), 2);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new ThrownAttribute());
	}

}
