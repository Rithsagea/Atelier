package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.equipment.Weapon;
import com.tempera.atelier.dnd.types.equipment.attributes.FinesseAttribute;
import com.tempera.atelier.dnd.types.equipment.attributes.RangeAttribute;
import com.tempera.atelier.dnd.types.equipment.attributes.ThrownAttribute;

@IndexedItem("phb-dart")
public class PHBDart extends Weapon {

	public PHBDart() {
		super("Dart", null, "PHB, page 149. Available in the SRD and the Basic Rules.", new Price(5, Currency.COPPER),
				.25);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.RANGE);
		addAttributes(new FinesseAttribute(), new RangeAttribute(), new ThrownAttribute());
	}

}
