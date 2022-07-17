package com.atelier.dnd.types.equipment.weapons;

import com.atelier.database.IndexedItem;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.FinesseAttribute;
import com.atelier.dnd.types.equipment.attributes.LightAttribute;

@IndexedItem("phb-shortsword")
public class PHBShortsword extends Weapon {
	public PHBShortsword() {
		super("Shortsword", null,
			"Source: PHB, page 149. Available in the SRD and the Basic Rules.",
			new Price(2, Currency.GOLD), 2);

		addCategories(EquipmentType.MARTIAL_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new FinesseAttribute(), new LightAttribute());
	}
}
