package com.atelier.dnd.types.character.races.traits;

import com.atelier.dnd.types.IndexedItem;
import com.atelier.dnd.types.character.DescriptionAttribute;
import com.atelier.dnd.types.enums.Size;

@IndexedItem("size-trait")
public class SizeTrait extends DescriptionAttribute {

	private Size size;
	
	public SizeTrait(Size size, String info) {
		super("Size", info);
		
		this.size = size;
	}
	
	public Size getSize() {
		return size;
	}
}
