package com.atelier.dnd.types.character;

import java.util.HashMap;
import java.util.Map;

import com.atelier.dnd.events.LoadEvent.LoadRaceEvent;
import com.atelier.dnd.events.LoadEvent.LoadSheetEvent;
import com.rithsagea.util.event.EventBus;
import com.rithsagea.util.event.EventHandler;

public abstract class CharacterRace implements AbstractRace {
	private Map<String, CharacterAttribute> traits = new HashMap<>();
	
	private transient final String id;
	private transient final String name;
	private transient EventBus eventBus = new EventBus();
	
	public CharacterRace(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	protected void addTrait(String key, CharacterAttribute attribute) {
		traits.put(id + "." + key, attribute);
	}

	protected CharacterAttribute removeTrait(String key) {
		return traits.remove(id + "." + key);
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
	public Map<String, CharacterAttribute> getTraits() {
		return traits;
	}
	
	@EventHandler
	private void onLoadSheet(LoadSheetEvent event) {
		eventBus.clearListeners();
		traits.values().forEach(eventBus::registerListener);
		eventBus.submitEvent(new LoadRaceEvent(this));
	}
}
