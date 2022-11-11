package com.atelier.dnd.character;

import com.atelier.dnd.events.LoadEvent.LoadCharacterClassEvent;
import com.rithsagea.util.event.EventHandler;

public abstract class ClassFeature extends CharacterAttribute {
	
	private transient CharacterClass characterClass;

	@EventHandler
	public void onLoadCharacterClass(LoadCharacterClassEvent event) {
		characterClass = event.getCharacterClass();
	}

	public CharacterClass getCharacterClass() {
		return characterClass;
	}
}
