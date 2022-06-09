package com.tempera.atelier.dnd.types.equipment.weapons;

import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.equipment.Weapon;
import com.tempera.atelier.dnd.types.equipment.attributes.VersatileAttribute;

@IndexedItem("phb-staff")
public class PHBStaff extends Weapon {

	public PHBStaff() {
		super("Staff",
				"An arcane focus is a special item designed to channel the power of arcane spells. A sorcerer, warlock, or wizard can use such an item as a spellcasting focus.",
				"PHB, page 151. Available in the SRD and the Basic Rules.", new Price(5, Currency.GOLD), 4);
		addCategories(EquipmentType.SIMPLE_WEAPON, EquipmentType.MELEE_WEAPON);
		addAttributes(new VersatileAttribute());
	}

}
