package com.atelier.dnd.character.attributes;

import com.atelier.dnd.events.LoadEvent.LoadCharacterRaceEvent;
import com.rithsagea.util.event.EventHandler;

public abstract class RacialTrait extends CharacterAttribute {
	
	private transient CharacterRace characterRace;
	
	@EventHandler
	private void onLoadCharacterRace(LoadCharacterRaceEvent e) {
		characterRace = e.getCharacterRace();
	}

	public CharacterRace getCharacterRace() {
		return characterRace;
	}
}
