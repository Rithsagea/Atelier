package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.equipment.Weapon;
import com.tempera.atelier.dnd.types.equipment.attributes.AmmunitionAttribute;
import com.tempera.atelier.dnd.types.equipment.attributes.HeavyAttribute;
import com.tempera.atelier.dnd.types.equipment.attributes.RangeAttribute;
import com.tempera.atelier.dnd.types.equipment.attributes.TwoHandedAttribute;

public class PHBLongbow extends Weapon{

	public PHBLongbow() {
		super("Longbow", null, "PHB, page 149. Available in the SRD and the Basic Rules.",
			new Price(50, Currency.GOLD), 2);
		
		addCategories(EquipmentType.MARTIAL_WEAPON);
		addAttributes(new RangeAttribute(), new AmmunitionAttribute(),
			new HeavyAttribute(), new TwoHandedAttribute());
	}

}
