package com.atelier.dnd.types.equipment.adventuring;

import com.atelier.database.IndexedItem;
import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.AdventuringGear;

@IndexedItem("phb-quiver")
public class PHBQuiver extends AdventuringGear {

	public PHBQuiver() {
		super("Quiver", "A quiver can hold up to 20 arrows.",
			"PHB, page 153. Available in the SRD and the Basic Rules.",
			new Price(1, Currency.GOLD), 1);
	}

}
