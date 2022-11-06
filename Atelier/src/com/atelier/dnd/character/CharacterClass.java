package com.atelier.dnd.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.atelier.database.AtelierObject;
import com.atelier.dnd.events.LoadEvent.LoadCharacterClassEvent;
import com.atelier.dnd.events.LoadEvent.LoadCharacterEvent;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.rithsagea.util.event.EventBus;
import com.rithsagea.util.event.EventHandler;
import com.rithsagea.util.event.Listener;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "_cls", defaultImpl = Void.class)
public abstract class CharacterClass implements AtelierObject, Listener {

	private transient EventBus eventBus = new EventBus();
	private int level = 0;
	
	private Map<String, ClassFeature> features = new HashMap<>();
	
	private transient List<Map<String, ClassFeature>> levelFeatureMap;

	public CharacterClass() {
		levelFeatureMap = new ArrayList<>();
		IntStream.range(0, 20).forEach(x -> levelFeatureMap.add(new HashMap<>()));

		init();
		features.putAll(levelFeatureMap.get(0));
	}

	/**
	 * Load class features here
	 * - Add all features to event listener
	 * - Send load event for classes to features.
	 */
	@EventHandler
	private void onLoadCharacter(LoadCharacterEvent event) {
		features.values().forEach(eventBus::registerListener);
		eventBus.submitEvent(new LoadCharacterClassEvent(this));
	}

	/**
	 * Register features gained through leveling for this class
	 */
	protected abstract void init();

	protected void registerFeature(int level, String key, ClassFeature feature) {
		levelFeatureMap.get(level).put(key, feature);
	}

	public String getName() { 
		return getProperty("name");
	}
	
	public int getLevel() {
		return level;
	}

	//TODO remove debug method
	@Deprecated
	public void levelUp() {
		level++;
		features.putAll(levelFeatureMap.get(level));
	}
	
	public Map<String, ClassFeature> getAttributes() {
		return Collections.unmodifiableMap(features);
	}
	
	@Override
	public String toString() {
		return getName() + " " + level;
	}
}
