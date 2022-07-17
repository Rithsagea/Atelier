package com.atelier.dnd.types.equipment.weapons;

import com.atelier.database.IndexedItem;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Weapon;
import com.atelier.dnd.types.equipment.attributes.VersatileAttribute;

@IndexedItem("phb-wooden-staff")
public class PHBWoodenStaff extends Weapon {

	public PHBWoodenStaff() {
		super("Wooden Staff", "A druid can use this object as a spellcasting focus.",
				"PHB, page 151. Available in the SRD and the Basic Rules.", new Price(5, Currency.GOLD), 4);
		addCategories(EquipmentType.SIMPLE_WEAPON);
		addAttributes(new VersatileAttribute());
	}

}
