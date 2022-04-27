package com.tempera.atelier.dnd.types.character;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class CharacterClass implements AbstractClass {
	
	private Map<String, Attribute> attributes = new HashMap<>();
	private int level = 1;
	
	protected void addAttribute(String key, Attribute attribute) {
		attributes.put(key, attribute);
	}
	
	protected Attribute removeAttribute(String key) {
		return attributes.remove(key);
	}
	
	@Override
	public int getLevel() {
		return level;
	}
	
	@Override
	public Collection<Attribute> getAttributes() {
		return attributes.values();
	}
	
	@Override
	public String toString() {
		return getName() + " " + level;
	}
}
