package com.atelier.dnd.types.equipment.weapons;

import com.atelier.database.Subtype;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.AmmunitionAttribute;
import com.atelier.dnd.types.equipment.attributes.RangeAttribute;

@Subtype("phb-sling")
public class PHBSling extends Weapon {

	public PHBSling() {
		super("Sling", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(1, Currency.SILVER),
				1);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.RANGE);
		addAttributes(new RangeAttribute(), new AmmunitionAttribute());
	}

}
