package com.atelier.dnd.character;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.atelier.AtelierObject;
import com.atelier.dnd.events.LoadEvent.LoadCharacterEvent;
import com.atelier.dnd.events.LoadEvent.LoadCharacterRaceEvent;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.rithsagea.util.event.EventBus;
import com.rithsagea.util.event.EventHandler;
import com.rithsagea.util.event.Listener;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "_cls", defaultImpl = Void.class)
public abstract class CharacterRace implements AtelierObject, Listener {
	
	private transient EventBus eventBus = new EventBus();

	private Map<String, RacialTrait> traits = new HashMap<>();

	public CharacterRace() {
		init();
	}

	/**
	 * Load class features here
	 * - Add all features to event listener
	 * - Send load event for classes to features.
	 */
	@EventHandler
	private void onLoadCharacter(LoadCharacterEvent event) {
		traits.values().forEach(eventBus::registerListener);
		eventBus.submitEvent(new LoadCharacterRaceEvent(this));
	}

	/**
	 * Register traits gained through leveling for this class
	 */
	protected abstract void init();

	protected void registerTrait(String key, RacialTrait trait) {
		traits.put(key, trait);
	}

	public String getName() { 
		return getProperty("name");
	}

	public Map<String, RacialTrait> getTraits() {
		return Collections.unmodifiableMap(traits);
	}

	@Override
	public String toString() {
		return getName();
	}
}
