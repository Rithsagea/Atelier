package com.tempera.atelier.dnd.types.equipment.adventuring;

import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.equipment.AdventuringGear;

public class PHBQuiver extends AdventuringGear{

	public PHBQuiver(String name, String description, String source, Price price, int weight) {
		super("Quiver", "A quiver can hold up to 20 arrows.", "PHB, page 153. Available in the SRD and the Basic Rules.", new Price(1, Currency.GOLD), 1);
	}

}
