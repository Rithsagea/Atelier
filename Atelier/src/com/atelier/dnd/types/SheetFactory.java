package com.atelier.dnd.types;

import java.util.UUID;

import com.atelier.database.Factory;

public class SheetFactory implements Factory<Sheet> {
	
	private UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");

	public void setId(UUID id) {
		this.id = id;
	}
	
	@Override
	public Sheet build() {
		return new Sheet(id);
	}
	
}
