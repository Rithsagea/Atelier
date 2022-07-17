package com.atelier.dnd.types.character.races.traits;

import com.atelier.database.IndexedItem;
import com.atelier.dnd.types.character.DescriptionAttribute;

@IndexedItem("trait-age")
public class AgeTrait extends DescriptionAttribute {

	public AgeTrait() {this(null);}
	
	public AgeTrait(String info) {
		super("Age", info);
	}

}
