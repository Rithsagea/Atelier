package com.atelier.dnd.types.equipment.weapons;

import com.atelier.database.IndexedItem;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.FinesseAttribute;
import com.atelier.dnd.types.equipment.attributes.RangeAttribute;
import com.atelier.dnd.types.equipment.attributes.ThrownAttribute;

@IndexedItem("phb-dart")
public class PHBDart extends Weapon {

	public PHBDart() {
		super("Dart", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(5, Currency.COPPER),
				.25);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.RANGE);
		addAttributes(new FinesseAttribute(), new RangeAttribute(), new ThrownAttribute());
	}

}
