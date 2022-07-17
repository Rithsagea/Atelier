package com.atelier.dnd.types.equipment.weapons;

import com.atelier.database.IndexedItem;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.FinesseAttribute;

@IndexedItem("phb-rapier")
public class PHBRapier extends Weapon {

	public PHBRapier() {
		super("Rapier", null,
			"PHB, page 149. Available in the SRD and the Basic Rules.",
			new Price(25, Currency.GOLD), 2);

		addCategories(EquipmentType.MARTIAL_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new FinesseAttribute());
	}
}
