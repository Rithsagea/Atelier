package com.tempera.atelier.dnd.types.equipment.ammunition;

import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.equipment.Ammunition;

public class PHBBolt extends Ammunition{

	public PHBBolt() {
		super("Bolt", null, "PHB, page 150. Available in the SRD and the Basic Rules.",
			new Price(5, Currency.COPPER), 0.075);
		}

}
