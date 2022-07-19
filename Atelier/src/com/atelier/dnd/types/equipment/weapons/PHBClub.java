package com.atelier.dnd.types.equipment.weapons;

import com.atelier.database.Subtype;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.LightAttribute;

@Subtype("phb-club")
public class PHBClub extends Weapon {
	public PHBClub() {
		super("Club", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(1, Currency.SILVER),
				2);

		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new LightAttribute());
	}

}
