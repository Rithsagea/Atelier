package com.atelier.dnd.types.equipment.weapons;

import com.atelier.database.Subtype;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.FinesseAttribute;
import com.atelier.dnd.types.equipment.attributes.LightAttribute;
import com.atelier.dnd.types.equipment.attributes.ThrownAttribute;

@Subtype("phb-dagger")
public class PHBDagger extends Weapon {

	public PHBDagger() {
		super("Dagger", null,
			"PHB, page 149. Available in the SRD and the Basic Rules.",
			new Price(2, Currency.GOLD), 1);
		
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new FinesseAttribute(), new LightAttribute(), new ThrownAttribute());
	}

	public PHBDagger(int amount) {
		this();
		setAmount(amount);
	}

}
