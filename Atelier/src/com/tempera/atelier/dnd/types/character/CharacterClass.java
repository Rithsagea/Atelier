package com.tempera.atelier.dnd.types.character;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class CharacterClass implements AbstractClass {
	
	private Set<Attribute> attributes = new HashSet<>();
	private int level = 1;
	
	protected void addAttribute(Attribute a) {
		attributes.add(a);
	}
	
	protected void removeAttribute(Attribute a) {
		attributes.remove(a);
	}
	
	@Override
	public int getLevel() {
		return level;
	}
	
	@Override
	public Set<Attribute> getAttributes() {
		return Collections.unmodifiableSet(attributes);
	}
	
	@Override
	public String toString() {
		return getName() + " " + level;
	}
}
