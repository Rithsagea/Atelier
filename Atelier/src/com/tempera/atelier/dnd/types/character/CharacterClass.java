package com.tempera.atelier.dnd.types.character;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class CharacterClass implements AbstractClass {
	
	private Map<String, Attribute> attributes = new HashMap<>();
	private int level = 1;
	
	private final String id;
	private final String name;
	
	public CharacterClass(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	protected void addAttribute(String key, Attribute attribute) {
		attributes.put(id + "." + key, attribute);
	}
	
	protected Attribute removeAttribute(String key) {
		return attributes.remove(id + "." + key);
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public int getLevel() {
		return level;
	}
	
	@Override
	public Map<String, Attribute> getAttributeMap() {
		return Collections.unmodifiableMap(attributes);
	}
	
	@Override
	public String toString() {
		return getName() + " " + level;
	}
}
