package com.atelier.dnd.types.equipment.weapons;

import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.AmmunitionAttribute;
import com.atelier.dnd.types.equipment.attributes.HeavyAttribute;
import com.atelier.dnd.types.equipment.attributes.RangeAttribute;
import com.atelier.dnd.types.equipment.attributes.TwoHandedAttribute;

public class PHBLongbow extends Weapon{

	public PHBLongbow() {
		super("Longbow", null, "PHB, page 149. Available in the SRD and the Basic Rules.",
			new Price(50, Currency.GOLD), 2);
		
		addCategories(EquipmentType.MARTIAL_WEAPON);
		addAttributes(new RangeAttribute(), new AmmunitionAttribute(),
			new HeavyAttribute(), new TwoHandedAttribute());
	}

}
