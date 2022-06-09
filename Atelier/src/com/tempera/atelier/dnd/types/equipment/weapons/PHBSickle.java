package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.equipment.Weapon;
import com.tempera.atelier.dnd.types.equipment.attributes.LightAttribute;

@IndexedItem("phb-sickle")
public class PHBSickle extends Weapon {

	public PHBSickle() {
		super("Sickle", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(1, Currency.GOLD),
				2);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new LightAttribute());
	}

}
