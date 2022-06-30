package com.atelier.dnd.types.equipment.weapons;

import com.atelier.dnd.types.IndexedItem;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.LightAttribute;

@IndexedItem("phb-sickle")
public class PHBSickle extends Weapon {

	public PHBSickle() {
		super("Sickle", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(1, Currency.GOLD),
				2);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new LightAttribute());
	}

}