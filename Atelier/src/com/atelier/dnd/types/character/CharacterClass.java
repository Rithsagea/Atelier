package com.atelier.dnd.types.character;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.atelier.dnd.events.LoadEvent.LoadClassEvent;
import com.atelier.dnd.events.LoadEvent.LoadSheetEvent;
import com.rithsagea.util.event.EventBus;
import com.rithsagea.util.event.EventHandler;

public abstract class CharacterClass implements AbstractClass {

	private Map<String, CharacterAttribute> features = new HashMap<>();
	private int level = 1;

	private transient final String id;
	private transient final String name;
	private transient EventBus eventBus = new EventBus();

	public CharacterClass(String id, String name) {
		this.id = id;
		this.name = name;
	}

	protected void addFeature(String key, CharacterAttribute feature) {
		features.put(id + "." + key, feature);
	}

	protected CharacterAttribute removeFeature(String key) {
		return features.remove(id + "." + key);
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
	public Map<String, CharacterAttribute> getFeatures() {
		return Collections.unmodifiableMap(features);
	}

	@Override
	public String toString() {
		return getName() + " " + level;
	}

	@EventHandler
	private void onLoadSheet(LoadSheetEvent event) {
		eventBus.clearListeners();
		features.values().forEach(eventBus::registerListener);
		eventBus.submitEvent(new LoadClassEvent(this));
	}
}
