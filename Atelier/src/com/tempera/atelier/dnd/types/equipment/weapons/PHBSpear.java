package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.equipment.Weapon;
import com.tempera.atelier.dnd.types.equipment.attributes.ThrownAttribute;
import com.tempera.atelier.dnd.types.equipment.attributes.VersatileAttribute;

@IndexedItem("phb-spear")
public class PHBSpear extends Weapon {

	public PHBSpear() {
		super("Spear", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(1, Currency.GOLD),
				3);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new ThrownAttribute(), new VersatileAttribute());
	}

}
