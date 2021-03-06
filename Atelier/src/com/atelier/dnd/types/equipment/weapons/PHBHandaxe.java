package com.atelier.dnd.types.equipment.weapons;

import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.LightAttribute;
import com.atelier.dnd.types.equipment.attributes.ThrownAttribute;

public class PHBHandaxe extends Weapon {

	public PHBHandaxe() {
		super("Handaxe", null,
			"PHB, page 149. Available in the SRD and the Basic Rules.",
			new Price(5, Currency.GOLD), 2);
		
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new LightAttribute(), new ThrownAttribute());
	}

}
