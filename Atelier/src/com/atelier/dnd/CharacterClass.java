package com.atelier.dnd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.atelier.AtelierObject;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.rithsagea.util.event.EventBus;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "_cls", defaultImpl = Void.class)
public abstract class CharacterClass implements AtelierObject {
	
	public static class TestClass extends CharacterClass {

	}

	private transient EventBus eventBus = new EventBus();
	private int level = 0;
	
	private Map<String, CharacterAttribute> attributes = new HashMap<>();
	
	public String getName() { 
		return getProperty("name");
	}
	
	public int getLevel() {
		return level;
	}
	
	public EventBus getEventBus() {
		return eventBus;
	}
	
	public Map<String, CharacterAttribute> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}
	
	@Override
	public String toString() {
		return getName() + " " + level;
	}
}
