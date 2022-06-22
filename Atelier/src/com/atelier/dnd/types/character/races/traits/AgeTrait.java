package com.atelier.dnd.types.character.races.traits;

import com.atelier.dnd.types.IndexedItem;
import com.atelier.dnd.types.character.DescriptionAttribute;

@IndexedItem("trait-age")
public class AgeTrait extends DescriptionAttribute {

	public AgeTrait(String info) {
		super("Age", info);
	}

}
