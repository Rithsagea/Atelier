package com.atelier.dnd.types.equipment.weapons;

import com.atelier.database.Subtype;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.VersatileAttribute;

@Subtype("phb-quarterstaff")
public class PHBQuarterstaff extends Weapon {

	public PHBQuarterstaff() {
		super("Quarterstaff", null, "PHB, page 149. Available in the SRD and the Basic Rules.",
				new Price(2, Currency.SILVER), 4);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new VersatileAttribute());
	}

}
