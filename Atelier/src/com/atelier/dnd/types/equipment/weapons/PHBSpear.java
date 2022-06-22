package com.atelier.dnd.types.equipment.weapons;

import com.atelier.dnd.types.IndexedItem;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.ThrownAttribute;
import com.atelier.dnd.types.equipment.attributes.VersatileAttribute;

@IndexedItem("phb-spear")
public class PHBSpear extends Weapon {

	public PHBSpear() {
		super("Spear", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(1, Currency.GOLD),
				3);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new ThrownAttribute(), new VersatileAttribute());
	}

}
