package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.equipment.Weapon;
import com.tempera.atelier.dnd.types.equipment.attributes.AmmunitionAttribute;
import com.tempera.atelier.dnd.types.equipment.attributes.LoadingAttribute;
import com.tempera.atelier.dnd.types.equipment.attributes.RangeAttribute;
import com.tempera.atelier.dnd.types.equipment.attributes.TwoHandedAttribute;

public class PHBLightCrossbow extends Weapon {

	public PHBLightCrossbow() {
		super("Light Crossbow", null,
			"PHB, page 149. Available in the SRD and the Basic Rules.",
			new Price(25, Currency.GOLD), 5);
		
		addCategories(EquipmentType.SIMPLE_WEAPON);
		addAttributes(new RangeAttribute(), new AmmunitionAttribute(),
			new LoadingAttribute(), new TwoHandedAttribute());
	}

}
