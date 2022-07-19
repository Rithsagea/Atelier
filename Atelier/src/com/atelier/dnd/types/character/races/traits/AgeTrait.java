package com.atelier.dnd.types.character.races.traits;

import com.atelier.database.Subtype;
import com.atelier.dnd.types.character.DescriptionAttribute;

@Subtype("trait-age")
public class AgeTrait extends DescriptionAttribute {

	public AgeTrait() {this(null);}
	
	public AgeTrait(String info) {
		super("Age", info);
	}

}
