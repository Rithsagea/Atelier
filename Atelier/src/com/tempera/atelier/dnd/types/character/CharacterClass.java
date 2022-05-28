package com.tempera.atelier.dnd.types.character;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rithsagea.util.event.EventBus;
import com.rithsagea.util.event.EventHandler;
import com.tempera.atelier.dnd.events.LoadClassEvent;
import com.tempera.atelier.dnd.events.LoadSheetEvent;

public abstract class CharacterClass implements AbstractClass {
	
	private Map<String, Attribute> attributes = new HashMap<>();
	private int level = 1;
	
	private transient final String id;
	private transient final String name;
	
	private transient EventBus eventBus;
	
	public CharacterClass(String id, String name) {
		this.id = id;
		this.name = name;
		
		eventBus = new EventBus();
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
	
	@EventHandler
	public void onLoadSheet(LoadSheetEvent event) {
		eventBus.clearListeners();
		attributes.values().forEach(eventBus::registerListener);
		eventBus.submitEvent(new LoadClassEvent(this));
	}
}
