package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.equipment.Weapon;
import com.tempera.atelier.dnd.types.equipment.attributes.LightAttribute;

@IndexedItem("phb-club")
public class PHBClub extends Weapon {
	public PHBClub() {
		super("Club", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(1, Currency.SILVER),
				2);

		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new LightAttribute());
	}

}
