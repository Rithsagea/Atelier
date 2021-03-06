package com.atelier.dnd.types.equipment.weapons;

import com.atelier.database.Subtype;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.TwoHandedAttribute;

@Subtype("phb-greatclub")
public class PHBGreatClub extends Weapon {

	public PHBGreatClub() {
		super("Greatclub", null, "PHB, page 149. Available in the SRD and the Basic Rules.",
				new Price(2, Currency.SILVER), 10);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new TwoHandedAttribute());
	}

}
