package com.atelier.dnd;

import com.atelier.AtelierObject;

public enum Ability implements AtelierObject {
	STRENGTH,
	DEXTERITY,
	CONSTITUTION,
	INTELLIGENCE,
	WISDOM,
	CHARISMA;
	
	private String name;
	
	private Ability() {
		name = getProperty("name");
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
