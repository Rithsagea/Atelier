package com.atelier.dnd.types.character.races.traits;

import com.atelier.database.Subtype;
import com.atelier.dnd.types.character.DescriptionAttribute;
import com.atelier.dnd.types.enums.Size;

@Subtype("size-trait")
public class SizeTrait extends DescriptionAttribute {

	private Size size;
	
	public SizeTrait() {this(null,null);}
	
	public SizeTrait(Size size, String info) {
		super("Size", info);
		
		this.size = size;
	}
	
	public Size getSize() {
		return size;
	}
}
