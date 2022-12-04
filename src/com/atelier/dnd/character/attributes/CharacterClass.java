package com.atelier.dnd.character.attributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.atelier.database.AtelierObject;
import com.atelier.dnd.character.LevelUpEvent;
import com.atelier.dnd.character.NullClass;
import com.atelier.dnd.events.LoadEvent.LoadCharacterClassEvent;
import com.atelier.dnd.events.LoadEvent.LoadCharacterEvent;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.rithsagea.util.event.EventBus;
import com.rithsagea.util.event.EventHandler;
import com.rithsagea.util.event.Listener;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "_cls", defaultImpl = NullClass.class)
public abstract class CharacterClass implements AtelierObject, Listener {

	private transient EventBus eventBus = new EventBus();
	private int level = 1;
	
	private Map<String, ClassFeature> features = new HashMap<>();

	public CharacterClass() {
		features.putAll(getFeatures(0));
		features.putAll(getFeatures(1));
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

	protected abstract Map<String, ClassFeature> getFeatures(int level);

	public String getName() { 
		return getProperty("name");
	}
	
	public int getLevel() {
		return level;
	}

	public void levelUp() {
		level++;
		features.putAll(getFeatures(level));
		eventBus.submitEvent(new LevelUpEvent(level));
	}
	
	public Map<String, ClassFeature> getFeatures() {
		return Collections.unmodifiableMap(features);
	}

	@Override
	public String toString() {
		return getName() + " " + level;
	}
}
