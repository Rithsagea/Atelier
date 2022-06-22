package com.atelier.dnd.types.character;

import com.atelier.discord.Menu;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rithsagea.util.event.Listener;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface CharacterAttribute extends Listener {

	public String getName();

	public Menu getMenu();

}
