package com.atelier.dnd.character;

import com.atelier.database.AtelierObject;
import com.atelier.discord.Menu;
import com.atelier.dnd.events.LoadEvent.LoadCharacterEvent;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.rithsagea.util.event.EventHandler;
import com.rithsagea.util.event.Listener;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "_cls", defaultImpl = Void.class)
public abstract class CharacterAttribute implements Listener, AtelierObject {

	private transient AtelierCharacter character;

	@EventHandler
	private void onLoadCharacter(LoadCharacterEvent event) {
		character = event.getCharacter();
	}

	public AtelierCharacter getCharacter() {
		return character;
	}

	public String getName() {
		return getProperty("name");
	}
	
	@Override
	public String toString() {
		return getName();
	}

	public abstract Menu getMenu();

}
